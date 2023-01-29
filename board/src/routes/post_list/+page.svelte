<script lang="ts">
  import { onMount } from "svelte";

  let postList: any = [];
  let page: number = 0

  async function getFirstPostList() {
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/post/page/" + page)
    const data = await dataRes.json()
    
    postList = data
  }
  
  async function getPostList() {
    const dataRes = await fetch("http://127.0.0.1:8080/api/v1/post/page/" + page)
    const data = await dataRes.json()
    
    postList = postList.concat(data)
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

  getFirstPostList();
</script>

{#each postList as post (post.id)}


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
            >Author: {post.user_name}</a
          >
        </p>
      </a>
    </div>
  </div>

{/each}

<style>
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
