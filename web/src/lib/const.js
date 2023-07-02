// Gets the public IP address
// of the computer running the
// code and stores it in the
// variable ip
export let ip
getPublicIP((ipAddress) => (ip = ipAddress))

function getPublicIP (callback) {
  fetch('https://api.ipify.org?format=json')
    .then((response) => response.json())
    .then((data) => callback(data.ip))
    .catch((error) => console.error(error))
}
