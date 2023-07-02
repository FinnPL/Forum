<script lang="ts">
  import Login from "$lib/Login/login.svelte";

    // data is a json string that contains the information about the user
    export let data: any;


    // decode the data from base64
    let infoString = atob(data.data);
    // parse the json string into a js object
    let infoJson = JSON.parse(infoString);

    // extract the information from the json object
    let givenname = infoJson.data.givenname;
    let surname = infoJson.data.surname;
    let classname = infoJson.data.class;
    let signature = infoJson.signature;
    let login = infoJson.data.login;
</script>

<!-- pass the information to the Login component -->
<Login givenname={givenname} surname={surname} classname={classname} signature={signature} login_name={login}/>