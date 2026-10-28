const form = document.getElementById("login-form");
const statusEl = document.getElementById("status");
const profileEl = document.getElementById("profile");

function showStatus(text, isError) {
  statusEl.hidden = false;
  statusEl.textContent = text;
  statusEl.classList.toggle("error", Boolean(isError));
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  profileEl.hidden = true;

  const body = {
    username: document.getElementById("username").value,
    password: document.getElementById("password").value,
  };

  const loginResponse = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });

  if (!loginResponse.ok) {
    showStatus("Login failed. Check the username and password.", true);
    return;
  }

  const auth = await loginResponse.json();
  showStatus("Logged in. Loading your profile with the JWT...");

  const meResponse = await fetch("/api/me", {
    headers: { Authorization: `${auth.tokenType} ${auth.token}` },
  });

  if (!meResponse.ok) {
    showStatus("The token was issued, but the protected request failed.", true);
    return;
  }

  const me = await meResponse.json();
  showStatus("Success. This page called a protected API with your JWT.");
  profileEl.hidden = false;
  profileEl.textContent = JSON.stringify({ user: me, token: auth.token }, null, 2);
});
