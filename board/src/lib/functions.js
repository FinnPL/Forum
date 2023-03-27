export function getCookie (cookieName) {
  const cookie = {}
  document.cookie.split(';').forEach(function (el) {
    const [key, value] = el.split('=')
    cookie[key.trim()] = value
  })
  return cookie[cookieName]
}
//function to get the cookie value with the cookie name