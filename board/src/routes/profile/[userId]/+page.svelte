<script lang="ts">
  import { onMount } from "svelte";
  export let data: any;
  let postList: any = [];
  let page = 0;
  let user_name:string
  let user_id:string
  console.log(data.userId)

  async function getUserDetails() {
    const fetchedDataRes:any = await fetch("http://127.0.0.1:8080/api/v1/user/" + data.userId)
    const fetchedData = await fetchedDataRes.json()
    
    user_name = fetchedData.user_name
    user_id = fetchedData.id
  }
  getUserDetails();

  async function getFirstPostList() {
    const fetchedDataRes = await fetch("http://127.0.0.1:8080/api/v1/post/user/" + data.userId +"/"+ page)
    const fetchedData = await fetchedDataRes.json()
    
    postList = fetchedData
  }
  getFirstPostList()

  async function getPostList() {
    const fetchedDataRes = await fetch("http://127.0.0.1:8080/api/v1/post/page/" + data.userId +"/"+ page)
    const fetchedData = await fetchedDataRes.json()
    
    postList = postList.concat(fetchedData)
  }
  onMount(async () => {
    window.onscroll = function (ev) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        page += 1;
        getPostList();
      }
    };
  });
</script>

<div class="container">
  <div class="alert alert-dark">
    <h2>Username: {user_name}</h2>
    <h2>Joined since: {user_id}</h2>
  </div>
</div>
<div class="profilePostList">
  {#each postList as post (post.id)}
   {#if post.title != undefined}
    <div class="container">
      <div class="alert alert-dark">
        <a href={"/post/" + post.id}>
          <h2>Title: {post.title}</h2>
          <p2>Body: {post.content}</p2><br />
          <br />
          <p2>Created: {post.date}</p2><br />
          <br />
          <p>
            <a href={"/profile/" + post.user_id}
              >Author: {post.date}</a
            >
          </p>
        </a>
      </div>
    </div>
    {/if}
  {/each}
</div>

<style>
  .container {
    max-width: 700px;
    margin: 0 auto;
    padding: 2rem;
    text-align: center;
  }
  .profilePostList a {
    text-decoration: none;
    color: inherit;
  }
</style>
