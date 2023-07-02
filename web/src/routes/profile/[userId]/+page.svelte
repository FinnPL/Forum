<script lang="ts">
  import { formatDate, getCookie } from "../../../lib/functions";
  import { onMount } from "svelte";
  import { own_user_id, token } from "../../../lib/Login/login";
  import { goto } from "$app/navigation";
  import PostItem from "$lib/PostItem.svelte";
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
  let canScroll = true;

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

    for(const post of fetchedData) {
      post.avatarSrc = avatarSrc;
      post.date = await formatDate(post.date);
    }

    postList = fetchedData;
    console.log(postList);
  }

  async function getPostList() {
    console.log(tokenValue+"SUSS")
    const fetchedDataRes = await fetch(
      ip + "api/v1/post/page/" + data.userId + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );

    const fetchedData = await fetchedDataRes.json();

    for(const post of fetchedData) {
      post.avatarSrc = avatarSrc;
      post.date = await formatDate(post.date);
    }

    console.log(fetchedData);

    

    postList = postList.concat(fetchedData);
    console.log(postList);
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 1000);
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
    location.reload();
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
      if (canScroll) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        page += 1;
        scrollTimeout();
        getPostList();
      }
    }};
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

<div class="container mx-auto py-5 max-w-5xl">
  <div class="alert alert-dark">
    <h2>Username: {user_name}</h2>
    {#if bio != null && bio != "null"}
      <h3>Bio: {bio}</h3>
    {/if}

    {#if avatarSrc != "data:"}
      <img src={avatarSrc} alt="Avatar" width="125" height="125" />
    {/if}

    {#if own_user_id_value == data.userId}
      <button class="bg-blue-500 text-white bg-ui hover:bg-hover px-4 py-2 rounded" on:click={toggle}>Profil bearbeiten</button>

      <div class={open ? "block" : "hidden"}>
        <div class="fixed inset-0 flex items-center justify-center">
          <div class="bg-border p-4 rounded w-96">
            <h2 class="text-lg font-bold mb-4">Profil bearbeiten</h2>
            <div class="mb-4">
                <textarea class="text-black" placeholder="Body" bind:value={bio_update} style="height: 100px"/>
            </div>
            <div class="mb-4">
              <input type="file" name="file" id="AvatarFile" bind:this={avatar_file} on:change={handleFileChange}/>
            </div>

            <div class="flex justify-end">
              <button class="bg-ui hover:bg-hover text-white px-4 py-2 rounded" on:click={toggle}>Cancel</button>
              <button class="bg-ui hover:bg-hover text-white px-4 py-2 rounded ml-2" on:click={toggle} on:click={update_bio} on:click={upload_avatar}>Update Profile</button>
            </div>
          </div>
        </div>
      </div>

    {/if}
  </div>
</div>

{#if postList[0] != undefined}
  {#each postList as post (post.id)}
    <PostItem post={post}/>
  {/each}
{/if}



