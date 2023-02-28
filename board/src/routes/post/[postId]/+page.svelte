<script lang="ts">
  import { Form, FormGroup } from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { Button } from "sveltestrap";
  import { onMount } from "svelte";
  import { token, cookie_name } from "../../../lib/Login/login";
  import { getCookie } from "../../../lib/functions";
  export let data: any;

  let tokenValue: string;

  let userID: string;
  let comment_text: string;
  let comment_list: any = [];
  let page: number = 0;

  let title: string;
  let content: string;
  let date: string;
  let user_name: string;
  let thisID: any = data.postId;
  let cookie_name_value;

  console.log(thisID);

  async function checkLoggedIn() {
    await subStores();
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
  }

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
    });

    cookie_name.subscribe((value: string) => {
      cookie_name_value = value;
    });
  }

  onMount(async () => {
    await checkLoggedIn();
    await subStores();
    getPost();
    getFirstComments();
  });

  async function getPost() {
    await subStores();
    const fetchedDataRes = await fetch(
      "http://127.0.0.1:8080/api/v1/post/" + thisID,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedDataRes.json();
    title = fetchedData.title;
    content = fetchedData.content;
    date = fetchedData.date;
    user_name = fetchedData.user_name;
    userID = fetchedData.user_id;
  }

  async function getFirstComments() {
    await subStores();
    const fetchedRes = await fetch(
      "http://127.0.0.1:8080/api/v1/comment/get/" + thisID + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedRes.json();

    comment_list = fetchedData;
  }

  async function getComments() {
    await subStores();
    const fetchedRes = await fetch(
      "http://127.0.0.1:8080/api/v1/comment/get/" + thisID + "/" + page,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + tokenValue,
        },
      }
    );
    const fetchedData = await fetchedRes.json();

    comment_list = comment_list.concat(fetchedData);
  }

  async function post_comment() {
    const res = await fetch("http://127.0.0.1:8080/api/v1/comment/add/", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + tokenValue,
      },
      body: JSON.stringify({
        content: comment_text,
        post_id: thisID,
      }),
    });
    console.log(res.json());
    return res.json();
  }

  onMount(async () => {
    window.onscroll = function (ev) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        page += 1;
        getComments();
      }
    };
  });
</script>

<div class="container">
  <div class="alert alert-dark">
    <h2>Title: {title}</h2>
    <p1>Body: {content}</p1><br />
    <br />
    <p2>Created: {date}</p2><br />
    <br />
    <p><a href={"/profile/" + userID}>Author: {user_name}</a></p>
    <br />

    <Form method="POST">
      <FormGroup>
        <h3>Comment:</h3>
        <div class="form-floating">
          <textarea
            class="form-control"
            placeholder="Body"
            bind:value={comment_text}
            id="comment_text"
            name="comment_text"
            style="height: 100px"
          />
          <label for="floatingTextarea2">Text</label>
        </div>
      </FormGroup>
      <div class="button">
        <Button on:click={post_comment}>Post Comment</Button>
      </div>
    </Form>
  </div>

  {#each comment_list as comment (comment.id)}
    <div class="alert alert-dark">
      <p2>Body: {comment.content}</p2><br />
      <br />
      <p>
        <a href={"/profile/" + comment.user_id}>Author: {comment.user_name}</a>
      </p>
    </div>
  {/each}
</div>

<style>
  .container {
    padding: 10px 16px;
    margin: 40px auto;
    max-width: 800px;
  }
  .container h2 {
    font-size: 30px;
    color: #3538f1d0;
    margin-bottom: 8px;
  }
  .container p1 {
    font-size: 20px;
  }
  .container a {
    text-decoration: none;
    color: inherit;
  }
  .button {
    text-align: center;
  }
</style>
