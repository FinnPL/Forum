<script lang="ts">
    import 'bootstrap/dist/css/bootstrap.min.css';
  import { onMount } from 'svelte';
    import {
      Alert,
    Button,
    Form,
    FormGroup,
    Input
  } from 'sveltestrap';
  import { token } from "../../lib/Login/login.js"


  let input:string;
  let searchList:any = [];
  let page:number = 0
  let searchType:string
  let tokenValue:string

  token.subscribe((value: string) => {
		tokenValue = value;
	});

  async function search_post() {
    searchType = "Post"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/post/search/" + input + "/" + page, {
      method: "GET",
      headers: { "Authorization": "Bearer "+ tokenValue}
    })
    const data = await dataRes.json()
    searchList = searchList.concat(data)
    
    console.log(searchList)
  }
  async function first_search_post() {
    searchType = "Post"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/post/search/" + input + "/" + page, {
      method: "GET",
      headers: { "Authorization": "Bearer "+ tokenValue}
    })
    const data = await dataRes.json()
    searchList = data;
    if(searchList.length == 0) {
      searchList[0] = "keinErgebnis"
    }
    console.log(searchList)
  }
  

async function search_user() {
  searchType = "User"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/user/search/" + input + "/" + page, {
      method: "GET",
      headers: { "Authorization": "Bearer "+ tokenValue}
    })
    const data = await dataRes.json()
    searchList = searchList.concat(data)
    console.log(searchList)
}
async function first_search_user() {
  searchType = "User"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/user/search/" + input + "/" + page, {
      method: "GET",
      headers: { "Authorization": "Bearer "+ tokenValue}
    })
    const data = await dataRes.json()
    searchList = data;
    if(searchList.length == 0) {
      searchList[0] = "keinErgebnis"
    }
    console.log(searchList)
}
onMount(async () => {
    window.onscroll = function (ev) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        
        if(searchType == "Post" ) {
          page += 1;
          search_post()
        }
        if(searchType == "User" ) {
          page += 1;
          search_user()
        }
      }
    };
  });

  async function resetPage() {
    page = 0
  }

</script>

<div class="container">
<div class="alert alert-dark" role="alert">

<h1>Suche</h1>



<Form>
    <FormGroup floating label="Gib hier Suchwörter ein">
      <Input required bind:value={input} />
    </FormGroup>
    
    
    {#if input } 
    <Button color="primary" on:click={resetPage} on:click={first_search_post} > Search Post </Button>
    <Button color="primary" on:click={resetPage} on:click={first_search_user}> Search User </Button>
    {:else}
    <div class="alert alert-warning" role="alert">
      Suchfeld darf nicht leer sein!
    </div>
  

    {/if}
    <div class="container">
    {#if searchList[0] == "keinErgebnis"}
    <div class="alert alert-warning" role="alert">
      Kein Ergebnis 
    </div>
    
  {/if}

</Form>
<br />


{#if searchType=="Post" && searchList[0] != "keinErgebnis"}

  {#each searchList as post (post.id)}
  <div class="alert alert-secondary" role="alert">
  
      <a href={"/post/" + post.id }>
      <h2> Title: {post.title}</h2>
      <p2>Body: {post.content}</p2><br />
      <br />
      <p2>Name: {post.user_name}</p2><br />
      <br />
      <p><a href={"/profile/" + post.user_id}>Author: {post.id}</a></p>
      </a>
    </div>
  {/each}

{/if}


{#if searchType=="User" && searchList[0] != "keinErgebnis"}
  

  {#each searchList as user (user.id)}
  <div class="resultlist">
  <div class="alert alert-secondary " role="alert">
  <a href={"/profile/" + user.id }> <br>
  <h2>Username: {user.user_name} </h2>
  </a>
  </div>
</div>
  {/each}

{/if}



</div>
</div>

<style>
  .container {
max-width: 700px;
margin: 0 auto;
padding: 2rem;
text-align: center;
  }
  .resultlist a {
    text-decoration: none;
    color: inherit;
  }
</style>