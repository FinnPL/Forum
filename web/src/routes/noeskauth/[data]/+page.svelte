<script lang="ts">
  import Login from "$lib/Login/login.svelte";

    export let data: any;

    let infoString = atob(data.data);
    console.log(infoString)
    let infoJson = JSON.parse(infoString);

    
    let givenname = infoJson.data.givenname;
    let surname = infoJson.data.surname;
    let classname = infoJson.data.class;
    let signature = infoJson.signature;
    let login = infoJson.data.login;
</script>

<Login givenname={givenname} surname={surname} classname={classname} signature={signature} login_name={login}/>
