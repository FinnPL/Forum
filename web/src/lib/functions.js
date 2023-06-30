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
    entity.avatarSrc = profilePictureMap.get(entity.user_id);
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
      entity.avatarSrc = URL.createObjectURL(blob);
    } else {
      entity.avatarSrc = null;
    }

    profilePictureMap.set(entity.user_id, entity.avatarSrc);
  }
}