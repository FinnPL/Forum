<script lang="ts">
  import { Form, FormGroup } from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { Button } from "sveltestrap";
  import {
    Modal,
    ModalBody,
    ModalFooter,
    Input,
    ModalHeader,
  } from "sveltestrap";
  import { onMount } from "svelte";
  import { token, cookie_name, own_user_id } from "../../../lib/Login/login";
  import { getCookie } from "../../../lib/functions";
  import { ip } from "../../../lib/const.js";
  import { error } from "@sveltejs/kit";
  import { goto } from "$app/navigation";

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
  let cookie_name_value: string;
  let own_user_id_value: string;
  let title_update: string;
  let content_update: string;

  let imageSrc:any = null;
  let file:any;
  let image_file:any;

  //Modal
  let open = false;
  const toggle = () => (open = !open);

  console.log(thisID);

  async function checkLoggedIn() {
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
    own_user_id_value = await getCookie("userid");
    own_user_id.set(own_user_id_value);
  }

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
    });

    cookie_name.subscribe((value: string) => {
      cookie_name_value = value;
    });
    own_user_id.subscribe((value: string) => {
      own_user_id_value = value;
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
    const fetchedDataRes = await fetch(ip + "api/v1/post/" + thisID, {
      method: "GET",
      headers: { Authorization: "Bearer " + tokenValue },
    });

    if (!fetchedDataRes.ok) {
      console.log("Test");
      throw error(404, {
        message: "Not found",
      });
    }

    const fetchedData = await fetchedDataRes.json();
    title = fetchedData.title;
    content = fetchedData.content;
    date = fetchedData.date;
    user_name = fetchedData.user_name;
    userID = fetchedData.user_id;
    title_update = title;
    content_update = content;
  }

  async function getFirstComments() {
    const fetchedRes = await fetch(
      ip + "api/v1/comment/get/" + thisID + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedRes.json();

    comment_list = fetchedData;
  }

  async function getComments() {
    const fetchedRes = await fetch(
      ip + "api/v1/comment/get/" + thisID + "/" + page,
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

  async function update_post() {
    console.log(
      thisID,
      title_update,
      content_update,
      own_user_id_value,
      cookie_name_value,
      date
    );
    const res = await fetch(ip + "api/v1/post/" + thisID, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + tokenValue,
      },
      body: JSON.stringify({
        id: thisID,
        title: title_update,
        content: content_update,
        user_id: own_user_id_value,
        user_name: cookie_name_value,
        date: "2023-03-04 14:00:05.0",
      }),
    });
    await update_image();
    return res.json();
  }

  async function post_comment() {
    const res = await fetch(ip + "api/v1/comment/add/", {
      method: "post",
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
    window.onscroll = function (ev) { // Dynamic site loading
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        page += 1;
        getComments();
      }
    };
  });

  onMount(async () => { 
  let bearerToken = await getCookie("tokenValue");
  console.log(bearerToken)
  let requestOptions:any = {
  method: 'GET',
  headers: { "Authorization": "Bearer " + bearerToken,},
  redirect: 'follow'
};

const path = window.location.pathname.split("/"); 
const post_id = path[path.length-1]; // Get the url params




  async function loadImage() {
const res = await fetch(ip + "api/v1/file/post/"+post_id+"?"+new Date().getTime(), requestOptions) // Add unique params to the image to avoid cache issues
const blob = await res.blob();
  const reader = new FileReader();
  reader.readAsDataURL(blob);
  reader.onloadend = (event) => {
    if(event.target && event.target.result) {
      imageSrc = event.target.result; 
    }
}
}
loadImage();
console.log(imageSrc)
})

const handleFileChange = (event:any) => {
    file = event.target.files[0];
  };

  async function update_image () {
  
    const formData = new FormData();
    formData.append('file', file);
    const res = await fetch(ip +"api/v1/file/post/" + thisID,{
      method: 'POST',
      body: formData,
      headers: { 
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res)
  };
  
  async function del_post() {
    const res = await fetch(ip +"api/v1/post/del/" + thisID,{
      method: 'DELETE',
      headers: { 
        Authorization: "Bearer " + tokenValue,
      }, 
    });
    console.log(res)
    await goto("/")
  }

</script>

<div class="container">
  <div class="alert alert-dark">
    <h2>{title}</h2>
    <p1>{content}</p1><br />
    <br />
    <p2>Datum: {date}</p2><br />
    <br />
    <p><a href={"/profile/" + userID}>Autor: {user_name}</a></p>
    {#if imageSrc != "data:" }
      <img src={imageSrc } alt="User Image" width="250" height="300">
    {/if}
    {#if own_user_id_value == userID}
      <div>
        <Button color="primary" on:click={toggle}>Edit Post</Button>
        <Button color="danger" on:click={del_post}>Delete Post</Button>
        <Modal isOpen={open} {toggle}>
          <ModalHeader {toggle}>Update Post</ModalHeader>
          <ModalBody>
            <Form>
              <FormGroup floating label="Titel">
                <Input placeholder="Titel" required bind:value={title_update} />
              </FormGroup>

              <FormGroup>
                <div class="form-floating">
                  <textarea
                    class="form-control"
                    placeholder="Body"
                    bind:value={content_update}
                    id="floatingTextarea2"
                    style="height: 100px"
                  />
                  <label for="floatingTextarea2">Text</label>
                </div>
              </FormGroup>
              <FormGroup>
                <Input type="file" name="file" id="AvatarFile" bind:this={image_file} on:change={handleFileChange} accept="image.png, image.jpeg, image.jpg"/>
              </FormGroup>
            </Form>
          </ModalBody>
          <ModalFooter>
            <Button
              color="primary"
              on:click={toggle}
              on:click={update_post}
              on:click={() => location.reload()}>Update Post</Button
            >
            <Button color="secondary" on:click={toggle}>Cancel</Button>
          </ModalFooter>
        </Modal>
      </div>

    {/if}
    <br />

    <Form>
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
        <Button color="primary" on:click={post_comment} on:click={() => location.reload()} >Post Comment</Button>
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
