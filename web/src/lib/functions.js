export function getCookie (cookieName) {
  const cookie = {}
  document.cookie.split(';').forEach(function (el) {
    const [key, value] = el.split('=')
    cookie[key.trim()] = value
  })
  return cookie[cookieName]
}
// function to get the cookie value with the cookie name

const profilePictureMap = new Map()

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
      ip + 'api/v1/file/profile/' + entity.user_id + "?" + new Date().getTime(),
      {
        method: 'GET',
        headers: { Authorization: 'Bearer ' + tokenValue },
      }
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

import { goto } from "$app/navigation";
import { get } from "svelte/store"

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
    { unit: 'Monat', value: 30 * 24 * 60 * 60 * 1000 },
    { unit: 'Tag', value: 24 * 60 * 60 * 1000 },
    { unit: 'Stunde', value: 60 * 60 * 1000 },
    { unit: 'Minute', value: 60 * 1000 },
  ];

  for (const duration of durations) {
    const value = Math.floor(diff / duration.value);
    if (value > 0) {
      return `vor ${value} ${duration.unit}${value > 1 ? 'n' : ''}`;
    }
  }

  return 'vor weniger als eine Minute';
}



export async function fetcher(url,method,body){
  const res = await fetch("http://" + location.hostname + ":8080/" + url ,{
    method:method,
    headers: { 
      "Content-Type": "application/json",
      Authorization: "Bearer " + await getCookie("tokenValue") 
    },
    body: JSON.stringify(body)
    
  })

  return await res.json()
}

export async function fetchPage(url,method,page){
  const res = await fetch("http://" + location.hostname + ":8080/"+ url+ page,{
    method:method,
    headers: { 
      "Content-Type": "application/json",
      Authorization: "Bearer " + await getCookie("tokenValue") },
  })

  return await res.json()
}
