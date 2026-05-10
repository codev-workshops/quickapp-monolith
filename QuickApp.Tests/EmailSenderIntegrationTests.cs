using System.Net.Http.Json;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using QuickApp.Server.Configuration;
using QuickApp.Server.Services.Email;

namespace QuickApp.Tests;

public class EmailSenderIntegrationTests : IAsyncLifetime
{
    private readonly HttpClient _smtp4devClient = new()
    {
        BaseAddress = new Uri("http://localhost:3000")
    };

    private JsonElement GetMessageArray(JsonElement root)
    {
        if (root.ValueKind == JsonValueKind.Array)
            return root;

        if (root.ValueKind == JsonValueKind.Object && root.TryGetProperty("results", out var results))
            return results;

        return root;
    }

    public async Task InitializeAsync()
    {
        // Clear all messages in smtp4dev before each test run
        var response = await _smtp4devClient.GetFromJsonAsync<JsonElement>("/api/Messages");
        var messages = GetMessageArray(response);
        if (messages.ValueKind == JsonValueKind.Array)
        {
            foreach (var msg in messages.EnumerateArray())
            {
                var id = msg.GetProperty("id").GetString();
                await _smtp4devClient.DeleteAsync($"/api/Messages/{id}");
            }
        }
    }

    public Task DisposeAsync() => Task.CompletedTask;

    private static EmailSender CreateEmailSender(string fromName = "Test Sender", string fromEmail = "sender@test.com")
    {
        var settings = new AppSettings
        {
            SmtpConfig = new SmtpConfig
            {
                Host = "localhost",
                Port = 2525,
                UseSSL = false,
                Name = fromName,
                EmailAddress = fromEmail,
                Username = "",  // smtp4dev doesn't require auth
                Password = ""
            }
        };

        var options = Options.Create(settings);
        var loggerFactory = LoggerFactory.Create(builder => builder.AddConsole());
        var logger = loggerFactory.CreateLogger<EmailSender>();

        return new EmailSender(options, logger);
    }

    [Fact]
    public async Task SendEmailAsync_WithRecipientOnly_DeliversToSmtp4dev()
    {
        // Arrange
        var sender = CreateEmailSender("QuickApp Template", "quickapp@test.com");
        var recipientName = "Test Recipient";
        var recipientEmail = "recipient@test.com";
        var subject = "Integration Test Email";
        var body = "<h1>Hello from integration test!</h1>";

        // Act
        var (success, errorMsg) = await sender.SendEmailAsync(
            recipientName, recipientEmail, subject, body, isHtml: true);

        // Assert - check return value
        Assert.True(success, $"SendEmailAsync failed: {errorMsg}");
        Assert.Null(errorMsg);

        // Assert - verify smtp4dev captured the email
        await Task.Delay(1000); // Give smtp4dev a moment to process
        var response = await _smtp4devClient.GetFromJsonAsync<JsonElement>("/api/Messages");
        var messages = GetMessageArray(response);
        Assert.True(messages.ValueKind == JsonValueKind.Array, "Expected an array of messages from smtp4dev");

        var messageArray = messages.EnumerateArray().ToList();
        Assert.Single(messageArray);

        var message = messageArray[0];
        Assert.Equal(subject, message.GetProperty("subject").GetString());
        Assert.Contains(recipientEmail, message.GetProperty("to").EnumerateArray().Select(e => e.GetString()).ToList());
    }

    [Fact]
    public async Task SendEmailAsync_WithSenderAndRecipient_DeliversToSmtp4dev()
    {
        // Arrange
        var sender = CreateEmailSender();
        var senderName = "Custom Sender";
        var senderEmail = "custom-sender@test.com";
        var recipientName = "Test Recipient";
        var recipientEmail = "recipient2@test.com";
        var subject = "Custom Sender Test";
        var body = "Plain text email body";

        // Act
        var (success, errorMsg) = await sender.SendEmailAsync(
            senderName, senderEmail, recipientName, recipientEmail, subject, body, isHtml: false);

        // Assert
        Assert.True(success, $"SendEmailAsync failed: {errorMsg}");
        Assert.Null(errorMsg);

        await Task.Delay(1000);
        var response = await _smtp4devClient.GetFromJsonAsync<JsonElement>("/api/Messages");
        var messages = GetMessageArray(response);
        var messageArray = messages.EnumerateArray().ToList();
        Assert.Single(messageArray);

        var message = messageArray[0];
        Assert.Equal(subject, message.GetProperty("subject").GetString());
        Assert.Contains(recipientEmail, message.GetProperty("to").EnumerateArray().Select(e => e.GetString()).ToList());
        Assert.Contains(senderEmail, message.GetProperty("from").GetString()!);
    }

    [Fact]
    public async Task SendEmailAsync_WithInvalidHost_ReturnsFailure()
    {
        // Arrange - point to a non-existent SMTP server
        var settings = new AppSettings
        {
            SmtpConfig = new SmtpConfig
            {
                Host = "nonexistent.invalid.host",
                Port = 9999,
                UseSSL = false,
                Name = "Test",
                EmailAddress = "test@test.com",
                Username = "",
                Password = ""
            }
        };

        var options = Options.Create(settings);
        var loggerFactory = LoggerFactory.Create(builder => builder.AddConsole());
        var logger = loggerFactory.CreateLogger<EmailSender>();
        var sender = new EmailSender(options, logger);

        // Act
        var (success, errorMsg) = await sender.SendEmailAsync(
            "Recipient", "recipient@test.com", "Test Subject", "Test Body");

        // Assert
        Assert.False(success);
        Assert.NotNull(errorMsg);
        Assert.NotEmpty(errorMsg);
    }

    [Fact]
    public async Task SendEmailAsync_HtmlEmail_ContainsHtmlBody()
    {
        // Arrange
        var sender = CreateEmailSender("QuickApp", "app@test.com");
        var htmlBody = "<html><body><h1>Welcome!</h1><p>This is an HTML email.</p></body></html>";

        // Act
        var (success, errorMsg) = await sender.SendEmailAsync(
            "HTML Recipient", "html-recipient@test.com",
            "HTML Email Test", htmlBody, isHtml: true);

        // Assert delivery succeeded
        Assert.True(success, $"SendEmailAsync failed: {errorMsg}");

        // Verify via smtp4dev API that the message was received
        await Task.Delay(1000);
        var response = await _smtp4devClient.GetFromJsonAsync<JsonElement>("/api/Messages");
        var messages = GetMessageArray(response);
        var messageArray = messages.EnumerateArray().ToList();
        Assert.Single(messageArray);

        // Fetch the full message body/source from smtp4dev
        var msgId = messageArray[0].GetProperty("id").GetString();
        var sourceResponse = await _smtp4devClient.GetStringAsync($"/api/Messages/{msgId}/source");
        Assert.Contains("Welcome!", sourceResponse);
        Assert.Contains("<h1>", sourceResponse);
    }
}
