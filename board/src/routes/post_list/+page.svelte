<script lang="ts">
  import { onMount } from "svelte";
  import { token } from "../../lib/Login/login";
  import { ip } from "../../lib/const.js"
  let postList: any = [];
  let page: number = 0;
  let tokenValue: string;


  token.subscribe((value: string) => {
    tokenValue = value;
  });

  async function getFirstPostList() {
    const dataRes = await fetch(
      ip + "api/v1/post/page/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const data = await dataRes.json();

    postList = data;
  }

  async function getPostList() {
    const dataRes = await fetch(
      ip + "api/v1/post/page/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const data = await dataRes.json();

    postList = postList.concat(data);
  }

  onMount(async () => {
    window.onscroll = function () {
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

{#if postList != undefined}
  {#each postList as post (post.id)}
    <div class="container">
      <div class="alert alert-dark">
        <a href={"/post/" + post.id}>
          <h2>{post.title}</h2>
          <p2>{post.content}</p2><br />
          <br />
          <p2>Datum: {post.date}</p2><br />
          <br />
          <p>
            <a href={"/profile/" + post.user_id}>Autor: {post.user_name}</a>
          </p>
        </a>
      </div>
    </div>
  {/each}
{/if}

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
