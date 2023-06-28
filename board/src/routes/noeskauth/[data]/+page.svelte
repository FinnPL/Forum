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
<div class="container">
<h1>{surname}</h1>
<h1>{givenname}</h1>
</div>

<Login givenname={givenname} surname={surname} classname={classname} signature={signature} login_name={login}/>

<style>
    .container {
      max-width: 400px;
      margin: 0 auto;
      padding: 2rem;
      text-align: center;
    }
  </style>