<script lang="ts">
    import 'bootstrap/dist/css/bootstrap.min.css';
    import {
    Button,
    Form,
    FormGroup,
    Input
  } from 'sveltestrap';

  let input:string;
  let searchList:any = [];
  let page:number = 0
  let searchType:string

  async function search_post() {
    searchType = "Post"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/post/search/" + input + "/" + page)
    const data = await dataRes.json()
    searchList = data;
    console.log(searchList)
  }
  

async function search_user() {
  searchType = "User"
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/user/search/" + input + "/" + page)
    const data = await dataRes.json()
    searchList = data;
    console.log(searchList)
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
    <Button on:click={search_post}> Search Post </Button>
    <Button on:click={search_user}> Search User </Button>
    {:else}
    <div class="alert alert-info" role="alert">
      Suchfeld darf nicht leer sein!
    </div>
    

    {/if}
</Form>
<br />


{#if searchType=="Post"}

  {#each searchList as post (post.id)}
  <div class="alert alert-dark" role="alert">
  
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


{#if searchType=="User"}
  
  {#each searchList as user (user.id)}
  <a href={"/profile/" + user.id }> <br>
  <h2>Username: {user.user_name} </h2>
  </a>


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
.container {
  padding: 10px 16px;
  margin: 40px auto;
  max-width: 800px;
}

.container h2 {
  font-size: 20px;
  color: #3538f1d0;
  margin-bottom: 8px;
}

.container a {
  text-decoration: none;
  color: inherit;
}

</style>