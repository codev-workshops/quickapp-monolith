#!/usr/bin/env python3
"""
Side-by-side comparison tests for QuickApp monolith vs microservices.
Compares auth, users, roles, customers, products, and orders endpoints.
"""

import json
import sys
import urllib.request
import urllib.error
import ssl
import time
from datetime import datetime

def load_config(path="config.json"):
    with open(path) as f:
        return json.load(f)

def make_request(url, method="GET", data=None, headers=None, verify_ssl=True):
    if headers is None:
        headers = {}
    headers.setdefault("Content-Type", "application/json")

    ctx = None
    if not verify_ssl:
        ctx = ssl.create_default_context()
        ctx.check_hostname = False
        ctx.verify_mode = ssl.CERT_NONE

    body = json.dumps(data).encode() if data else None
    req = urllib.request.Request(url, data=body, headers=headers, method=method)

    try:
        resp = urllib.request.urlopen(req, context=ctx, timeout=10)
        return {
            "status": resp.status,
            "body": json.loads(resp.read().decode()),
            "error": None
        }
    except urllib.error.HTTPError as e:
        return {
            "status": e.code,
            "body": None,
            "error": str(e)
        }
    except Exception as e:
        return {
            "status": None,
            "body": None,
            "error": str(e)
        }

class ComparisonTest:
    def __init__(self, config):
        self.config = config
        self.monolith = config["monolith"]
        self.microservices = config["microservices"]
        self.credentials = config["credentials"]
        self.results = []
        self.monolith_token = None
        self.microservices_token = None
        self.is_monolith_https = self.monolith["baseUrl"].startswith("https")

    def log(self, test_name, status, monolith_result, micro_result, details=""):
        result = {
            "test": test_name,
            "status": status,
            "monolith": {
                "status_code": monolith_result.get("status"),
                "error": monolith_result.get("error"),
                "data_count": len(monolith_result["body"]) if isinstance(monolith_result.get("body"), list) else None
            },
            "microservices": {
                "status_code": micro_result.get("status"),
                "error": micro_result.get("error"),
                "data_count": len(micro_result["body"]) if isinstance(micro_result.get("body"), list) else None
            },
            "details": details
        }
        self.results.append(result)
        icon = "PASS" if status == "pass" else ("FAIL" if status == "fail" else "SKIP")
        print(f"  [{icon}] {test_name}: {details}")

    def test_auth(self):
        print("\n=== Auth Tests ===")

        # Monolith login (OAuth2 password grant)
        mono_login = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["loginEndpoint"]}',
            method="POST",
            data={
                "grant_type": "password",
                "username": self.credentials["username"],
                "password": self.credentials["password"],
                "scope": "openid email phone profile offline_access roles"
            },
            verify_ssl=not self.is_monolith_https
        )

        # Microservices login (JWT)
        micro_login = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["loginEndpoint"]}',
            method="POST",
            data={
                "username": self.credentials["username"],
                "password": self.credentials["password"]
            }
        )

        if mono_login["body"] and "access_token" in (mono_login["body"] or {}):
            self.monolith_token = mono_login["body"]["access_token"]
        if micro_login["body"] and "access_token" in (micro_login["body"] or {}):
            self.microservices_token = micro_login["body"]["access_token"]

        mono_ok = self.monolith_token is not None
        micro_ok = self.microservices_token is not None
        status = "pass" if mono_ok and micro_ok else ("partial" if mono_ok or micro_ok else "fail")
        self.log("Login", status, mono_login, micro_login,
                 f"Monolith: {'OK' if mono_ok else 'FAILED'}, Microservices: {'OK' if micro_ok else 'FAILED'}")

    def _auth_headers(self, token):
        return {"Authorization": f"Bearer {token}", "Content-Type": "application/json"} if token else {}

    def test_users(self):
        print("\n=== Users Tests ===")
        mono = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["usersEndpoint"]}',
            headers=self._auth_headers(self.monolith_token),
            verify_ssl=not self.is_monolith_https
        )
        micro = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["usersEndpoint"]}',
            headers=self._auth_headers(self.microservices_token)
        )
        mono_count = len(mono["body"]) if isinstance(mono.get("body"), list) else 0
        micro_count = len(micro["body"]) if isinstance(micro.get("body"), list) else 0
        status = "pass" if mono_count > 0 and micro_count > 0 else "fail"
        self.log("List Users", status, mono, micro,
                 f"Monolith: {mono_count} users, Microservices: {micro_count} users")

    def test_roles(self):
        print("\n=== Roles Tests ===")
        mono = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["rolesEndpoint"]}',
            headers=self._auth_headers(self.monolith_token),
            verify_ssl=not self.is_monolith_https
        )
        micro = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["rolesEndpoint"]}',
            headers=self._auth_headers(self.microservices_token)
        )
        mono_count = len(mono["body"]) if isinstance(mono.get("body"), list) else 0
        micro_count = len(micro["body"]) if isinstance(micro.get("body"), list) else 0
        status = "pass" if mono_count > 0 and micro_count > 0 else "fail"
        self.log("List Roles", status, mono, micro,
                 f"Monolith: {mono_count} roles, Microservices: {micro_count} roles")

    def test_customers(self):
        print("\n=== Customer Tests ===")
        mono = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["customersEndpoint"]}',
            headers=self._auth_headers(self.monolith_token),
            verify_ssl=not self.is_monolith_https
        )
        micro = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["customersEndpoint"]}',
            headers=self._auth_headers(self.microservices_token)
        )
        mono_count = len(mono["body"]) if isinstance(mono.get("body"), list) else 0
        micro_count = len(micro["body"]) if isinstance(micro.get("body"), list) else 0
        status = "pass" if mono_count > 0 and micro_count > 0 else "fail"
        self.log("List Customers", status, mono, micro,
                 f"Monolith: {mono_count} customers, Microservices: {micro_count} customers")

    def test_products(self):
        print("\n=== Product Tests ===")
        mono = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["productsEndpoint"]}',
            headers=self._auth_headers(self.monolith_token),
            verify_ssl=not self.is_monolith_https
        )
        micro = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["productsEndpoint"]}',
            headers=self._auth_headers(self.microservices_token)
        )
        mono_count = len(mono["body"]) if isinstance(mono.get("body"), list) else 0
        micro_count = len(micro["body"]) if isinstance(micro.get("body"), list) else 0
        status = "pass" if mono_count > 0 and micro_count > 0 else "fail"
        self.log("List Products", status, mono, micro,
                 f"Monolith: {mono_count} products, Microservices: {micro_count} products")

    def test_orders(self):
        print("\n=== Order Tests ===")
        mono = make_request(
            f'{self.monolith["baseUrl"]}{self.monolith["ordersEndpoint"]}',
            headers=self._auth_headers(self.monolith_token),
            verify_ssl=not self.is_monolith_https
        )
        micro = make_request(
            f'{self.microservices["baseUrl"]}{self.microservices["ordersEndpoint"]}',
            headers=self._auth_headers(self.microservices_token)
        )
        mono_count = len(mono["body"]) if isinstance(mono.get("body"), list) else 0
        micro_count = len(micro["body"]) if isinstance(micro.get("body"), list) else 0
        status = "pass" if mono_count > 0 and micro_count > 0 else "fail"
        self.log("List Orders", status, mono, micro,
                 f"Monolith: {mono_count} orders, Microservices: {micro_count} orders")

    def generate_report(self):
        report = {
            "generated_at": datetime.now().isoformat(),
            "endpoint_mapping": {
                "login": f'{self.monolith["loginEndpoint"]} -> {self.microservices["loginEndpoint"]}',
                "users": f'{self.monolith["usersEndpoint"]} -> {self.microservices["usersEndpoint"]}',
                "roles": f'{self.monolith["rolesEndpoint"]} -> {self.microservices["rolesEndpoint"]}',
                "customers": f'{self.monolith["customersEndpoint"]} -> {self.microservices["customersEndpoint"]}',
                "products": f'{self.monolith["productsEndpoint"]} -> {self.microservices["productsEndpoint"]}',
                "orders": f'{self.monolith["ordersEndpoint"]} -> {self.microservices["ordersEndpoint"]}'
            },
            "results": self.results,
            "summary": {
                "total": len(self.results),
                "passed": sum(1 for r in self.results if r["status"] == "pass"),
                "failed": sum(1 for r in self.results if r["status"] == "fail"),
                "partial": sum(1 for r in self.results if r["status"] == "partial")
            }
        }

        with open("comparison-report.json", "w") as f:
            json.dump(report, f, indent=2)

        # Generate markdown report
        md = f"# Comparison Report\n\nGenerated: {report['generated_at']}\n\n"
        md += "## Endpoint Mapping\n\n| Resource | Monolith | Microservices |\n|----------|----------|---------------|\n"
        for resource, mapping in report["endpoint_mapping"].items():
            old, new = mapping.split(" -> ")
            md += f"| {resource} | `{old}` | `{new}` |\n"

        md += "\n## Test Results\n\n| Test | Status | Details |\n|------|--------|----------|\n"
        for r in self.results:
            icon = "PASS" if r["status"] == "pass" else ("FAIL" if r["status"] == "fail" else "PARTIAL")
            md += f"| {r['test']} | {icon} | {r['details']} |\n"

        md += f"\n## Summary\n\n- Total: {report['summary']['total']}\n"
        md += f"- Passed: {report['summary']['passed']}\n"
        md += f"- Failed: {report['summary']['failed']}\n"
        md += f"- Partial: {report['summary']['partial']}\n"

        with open("comparison-report.md", "w") as f:
            f.write(md)

        print(f"\n{'='*50}")
        print(f"Report generated: comparison-report.json, comparison-report.md")
        print(f"Total: {report['summary']['total']}, Passed: {report['summary']['passed']}, "
              f"Failed: {report['summary']['failed']}, Partial: {report['summary']['partial']}")

    def run_all(self):
        print("QuickApp Monolith vs Microservices Comparison")
        print("=" * 50)
        self.test_auth()
        self.test_users()
        self.test_roles()
        self.test_customers()
        self.test_products()
        self.test_orders()
        self.generate_report()


if __name__ == "__main__":
    config_path = sys.argv[1] if len(sys.argv) > 1 else "config.json"
    config = load_config(config_path)
    tester = ComparisonTest(config)
    tester.run_all()
