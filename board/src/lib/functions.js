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
 * @param {{ user_id: string; avatarSrc: string | null; }} post
 */
export async function fetchProfilePicture (ip, tokenValue, post) {
  if (profilePictureMap.has(post.user_id)) {
    post.avatarSrc = profilePictureMap.get(post.user_id)
  } else {
    const profilePictureRes = await fetch(
      ip + 'api/v1/file/profile/' + post.user_id,
      {
        method: 'GET',
        headers: { Authorization: 'Bearer ' + tokenValue }
      }
    )

    if (profilePictureRes.ok) {
      const blob = await profilePictureRes.blob()
      post.avatarSrc = URL.createObjectURL(blob)
    } else {
      post.avatarSrc = null
    }

    profilePictureMap.set(post.user_id, post.avatarSrc)
  }
}
