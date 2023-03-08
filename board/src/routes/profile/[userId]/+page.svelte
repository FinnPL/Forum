<script lang="ts">
  import { getCookie } from "$lib/functions";
  import { onMount } from "svelte";
  import { own_user_id, token } from "../../../lib/Login/login";
  import { ip } from "../../../lib/const.js"
  import { FormGroup, Input, FormText, Label, Button } from "sveltestrap";
  export let data: any;
  let postList: any = [];
  let page = 0;
  let user_name: string;
  let tokenValue: string;
  let file:any ;
  let own_user_id_value:string
  let avatarSrc:any = null;
  let avatar_file:any


  async function checkLoggedIn() {

    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);

    own_user_id_value = await getCookie("userid");
    own_user_id.set(own_user_id_value);
  }

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
    });
    own_user_id.subscribe((value: string) => {
      own_user_id_value = value;
    });
  }


  onMount(async () => {
    await checkLoggedIn();
    await subStores();
    console.log(avatarSrc)
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

  const handleFileChange = (event:any) => {
    file = event.target.files[0];
  };

  async function upload_avatar () {
  
    const formData = new FormData();
    formData.append('file', file);
    const res = await fetch(ip +"api/v1/file/profile",{
      method: 'POST',
      body: formData,
      headers: { 
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res)
  };


onMount(async () => { 
  let bearerToken = await getCookie("tokenValue");
  console.log(bearerToken)
  let requestOptions:any = {
  method: 'GET',
  headers: { "Authorization": "Bearer " + bearerToken,},
  redirect: 'follow'
};

const path = window.location.pathname.split("/");
const userid = path[path.length-1];




  async function loadAvatar() {
const res = await fetch("http://127.0.0.1:8080/api/v1/file/profile/"+userid, requestOptions)
const blob = await res.blob();
  const reader = new FileReader();
  reader.readAsDataURL(blob);
  reader.onloadend = (event) => {
    if(event.target && event.target.result) {
      avatarSrc = event.target.result; 
    }
}
}
loadAvatar();
console.log(avatarSrc)
})


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

    {#if avatarSrc}
      <img src={avatarSrc } alt="Avatar" width="250" height="300">
    {/if}
    {#if own_user_id_value == data.userId}
    
    <FormGroup>
      <Input type="file" name="file" id="AvatarFile" bind:this={avatar_file} on:change={handleFileChange} accept="image.png, image.jpeg, image.jpg"/>
      <Button color="primary" on:click={upload_avatar} on:click={() => location.reload()} >Hochladen</Button>
    </FormGroup>
    {/if}

  </div>
</div>
<div class="container">
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
