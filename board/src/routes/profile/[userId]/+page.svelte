<script lang="ts">
  import { getCookie } from "../../../lib/functions";
  import { onMount } from "svelte";
  import { own_user_id, token } from "../../../lib/Login/login";
  import { goto } from "$app/navigation";
  export let data: any;
  let postList: any = [];
  let page = 0;
  let user_name: string;
  let bio: string;
  let bio_update: string;
  let tokenValue: string;
  let file: any;
  let own_user_id_value: string;
  let avatarSrc: any = null;
  let avatar_file: any;
  //Modal
  let open = false;
  const toggle = () => (open = !open);
  let ip: string;

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

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
    await get_server_ip();
    await checkLoggedIn();
    await subStores();
    console.log(avatarSrc);
    getUserDetails();
    getFirstPostList();
  });

  async function getUserDetails() {
    const fetchedDataRes: any = await fetch(ip + "api/v1/user/" + data.userId, {
      method: "GET",
      headers: { Authorization: "Bearer " + tokenValue },
    });
    const fetchedData = await fetchedDataRes.json();
    console.log(fetchedDataRes);
    user_name = fetchedData.user_name;
    bio = fetchedData.bio;
    if (bio != "null" && bio != null) {
      bio_update = bio; // For the modal pre-input
    } else {
      bio_update = "";
    }
    console.log(bio);
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

  const handleFileChange = (event: any) => {
    file = event.target.files[0];
  };

  async function upload_avatar() {
    const formData = new FormData();
    formData.append("file", file);
    const res = await fetch(ip + "api/v1/file/profile", {
      method: "POST",
      body: formData,
      headers: {
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res);
    await goto("/");
    await goto("/profile/" + own_user_id_value);
  }

  onMount(async () => {
    let bearerToken = await getCookie("tokenValue");
    console.log(bearerToken);
    let requestOptions: any = {
      method: "GET",
      headers: { Authorization: "Bearer " + bearerToken },
      redirect: "follow",
    };
    const path = window.location.pathname.split("/");
    const userid = path[path.length - 1]; // Get userid from url path

    async function loadAvatar() {
      const res = await fetch(
        ip + "api/v1/file/profile/" + userid + "?" + new Date().getTime(),
        requestOptions
      );
      const blob = await res.blob();
      const reader = new FileReader();
      reader.readAsDataURL(blob);
      reader.onloadend = (event) => {
        if (event.target && event.target.result) {
          avatarSrc = event.target.result;
        }
        console.log(avatarSrc);
      };
    }
    loadAvatar();
    console.log(avatarSrc);
  });

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

  async function update_bio() {
    const res = await fetch(ip + "api/v1/user/update/" + bio_update, {
      method: "PUT",
      headers: {
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res);
  }
</script>

<div class="container">
  <div class="alert alert-dark">
    <h2>Username: {user_name}</h2>
    {#if bio != null && bio != "null"}
      <h3>Bio: {bio}</h3>
    {/if}

    {#if avatarSrc != "data:"}
      <img src={avatarSrc} alt="Avatar" width="250" height="300" />
    {/if}

    {#if own_user_id_value == data.userId}
      <FormGroup>
        <Button color="info" on:click={toggle}>Edit Profile</Button>
      </FormGroup>
      <Modal isOpen={open} {toggle}>
        <ModalHeader {toggle}>Edit Profile</ModalHeader>
        <ModalBody>
          <Form>
            <FormGroup>
              <div class="form-floating">
                <textarea
                  class="form-control"
                  placeholder="Body"
                  bind:value={bio_update}
                  id="floatingTextarea2"
                  style="height: 100px"
                />
                <label for="floatingTextarea2">Text</label>
              </div>
            </FormGroup>
            <FormGroup>
              <Input
                type="file"
                name="file"
                id="AvatarFile"
                bind:this={avatar_file}
                on:change={handleFileChange}
              />
            </FormGroup>
          </Form>
        </ModalBody>
        <ModalFooter>
          <Button
            color="primary"
            on:click={toggle}
            on:click={update_bio}
            on:click={upload_avatar}>Update Profile</Button
          >
          <Button color="secondary" on:click={toggle}>Cancel</Button>
        </ModalFooter>
      </Modal>
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
