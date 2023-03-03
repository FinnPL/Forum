<script lang="ts">
  import { getCookie } from "$lib/functions";
  import { onMount } from "svelte";
  import { token } from "../../../lib/Login/login";
  import { ip } from "../../../lib/const.js"
  export let data: any;
  let postList: any = [];
  let page = 0;
  let user_name: string;
  let tokenValue: string;


  async function checkLoggedIn() {
    await subStores();

    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
  }

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
    });
  }

  onMount(async () => {
    await checkLoggedIn();
    await subStores();
    getUserDetails();
    getFirstPostList();
  });

  async function getUserDetails() {
    const fetchedDataRes: any = await fetch(
      ip + "api/v1/user/" + data.userId,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedDataRes.json();
    console.log(fetchedDataRes);
    user_name = fetchedData.user_name;
    console.log(user_name);
  }

  async function getFirstPostList() {
    const fetchedDataRes = await fetch(
      ip + "api/v1/post/user/" + data.userId + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedDataRes.json();
    postList = fetchedData;
    console.log(postList);
  }

  async function getPostList() {
    const fetchedDataRes = await fetch(
      ip + "api/v1/post/page/" + data.userId + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedDataRes.json();
    console.log(fetchedData);
    postList = postList.concat(fetchedData);
    console.log(postList);
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

  </div>
</div>
<div class="profilePostList">
  {#if postList[0] != undefined}
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
              <a href={"/profile/" + post.user_id}>Author: {post.date}</a>
            </p>
          </a>
        </div>
      </div>
    {/each}
  {/if}
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
