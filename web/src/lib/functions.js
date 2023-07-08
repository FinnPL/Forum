import {goto} from "$app/navigation";

/* This is an example for calling this function: getCookie('cookieName'); */
export function getCookie(cookieName) {
  const cookie = {};
  document.cookie.split(";").forEach(function (el) {
    const [key, value] = el.split("=");
    cookie[key.trim()] = value;
  });
  return cookie[cookieName];
}

const profilePictureMap = new Map();

/**
 * @param {string} ip
 * @param {string} tokenValue
 * @param {{ user_id: string; avatarSrc: string | null; }} entity
 */
export async function fetchProfilePicture(ip, tokenValue, entity) {
  if (profilePictureMap.has(entity.user_id)) {
    return profilePictureMap.get(entity.user_id);
  } else {
    const profilePictureRes = await fetch(
      ip + "api/v1/file/profile/" + entity.user_id + "?" + new Date().getTime(),
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      },
    );

    if (profilePictureRes.ok) {
      const blob = await profilePictureRes.blob();
      profilePictureMap.set(entity.user_id, URL.createObjectURL(blob));
      return URL.createObjectURL(blob);
    } else {
      return null;
    }
  }
}

/* The code below does the following:
1. Deletes all cookies.
2. Redirects to the home page.
3. Reloads the page. */

/* This is an example for calling this function:
signOut(); */
export async function signOut() {
  document.cookie.split(";").forEach(function (c) {
    document.cookie = c
      .replace(/^ +/, "")
      .replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); // Sets every cookie as expired to delete them
  });
  await goto("/");
  location.reload();
}

/**
 *
 * @param {string} dateString
 * @returns
 */

export async function formatDate(dateString) {
  const date = new Date(dateString);
  const now = new Date();
  const diff = now.getTime() - date.getTime();

  const durations = [
    { unit: "Monat", value: 30 * 24 * 60 * 60 * 1000 },
    { unit: "Tag", value: 24 * 60 * 60 * 1000 },
    { unit: "Stunde", value: 60 * 60 * 1000 },
    { unit: "Minute", value: 60 * 1000 },
  ];

  for (const duration of durations) {
    const value = Math.floor(diff / duration.value);
    const suffix =
      duration.unit === "Monat" || duration.unit === "Tag" ? "en" : "n";

    if (value > 0) {
      return `vor ${value} ${duration.unit}${value > 1 ? suffix : ""}`;
    }
  }

  return "vor weniger als eine Minute";
}

/* The code below does the following:
1. Creates a function called fetcher that takes three parameters: url, method, and body.
2. The function fetches the url, with the method and body passed in as parameters.
3. The function returns a json object of the fetched data. */

export async function fetcher(url, method, body) {
  const res = await fetch("http://" + location.hostname + ":8080/" + url, {
    method,
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + (await getCookie("tokenValue")),
    },
    body: JSON.stringify(body),
  });

  return await res.json();
}

/* The code below does the following:
1. Imports the fetch function from the browser.
2. Creates a function that takes three parameters: url, method, and page.
3. Sends a request to the backend API using the fetch function and returns the response.
4. Converts the response to JSON and returns it. */

/* This is an example for calling this function: fetchPage("api/page/", "GET", page) */
export async function fetchPage(url, method, page) {
  const res = await fetch(
    "http://" + location.hostname + ":8080/" + url + page,
    {
      method,
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + (await getCookie("tokenValue")),
      },
    },
  );

  return await res.json();
}

/**
 * @param {string} content
 */
export function formatContentWithLinks(content) {
  const urlRegex = /(https?:\/\/[^\s]+)/g;
  return content.replace(urlRegex, '<a class="text-primary underline" href="$1" target="_blank">$1</a>');
}
