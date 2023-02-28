const TOKEN_KEY = 'auth_token';
const USER_KEY = 'user_details';

export function setAuthToken(token) {
    if (typeof window !== "undefined")
        localStorage.setItem(TOKEN_KEY, token);
}

export function getAuthToken() {
    if (typeof window !== "undefined")
      return localStorage.getItem(TOKEN_KEY);
    return null;
}

export function removeAuthToken() {
    if (typeof window !== "undefined")
  localStorage.removeItem(TOKEN_KEY);
}

export function setUserDetails(user) {
    if (typeof window !== "undefined")
        localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getUserDetails() {
    if (typeof window !== "undefined")
        var user = localStorage.getItem(USER_KEY);
  return user ? JSON.parse(user) : null;
}

export function removeUserDetails() {
    if (typeof window !== "undefined")
        localStorage.removeItem(USER_KEY);
}
