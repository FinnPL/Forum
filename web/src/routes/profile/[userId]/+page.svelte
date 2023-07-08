<script lang="ts">
  import { fetchPage, fetcher, formatDate, getCookie } from "../../../lib/functions";
  import { onMount } from "svelte";
  import { goto } from "$app/navigation";
  import PostItem from "$lib/PostItem.svelte";
  import { default as defaultAvatar } from "../../../lib/assets/defaultAvatar.png";
  import { store_token, store_userid } from "$lib/stores";
  import  ScrollButton from "$lib/ScrollButton/+page.svelte";
  export let data: any;

  let postList: any = [];
  let page = 0;
  let bio: string;
  let bio_update: string;
  let file: any;
  let avatarSrc: any = undefined;
  let user_name: string;
  let avatar_file: any;
  //Modal
  let open = false;
  const toggle = () => (open = !open);
  let ip: string;
  let canScroll = true;


  $: {
    if(avatarSrc === "data:") avatarSrc = undefined;
  }

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  async function checkLoggedIn() {
    $store_token = await getCookie("tokenValue");
    $store_userid = await getCookie("userid");

  }

  

  onMount(async () => {
    await get_server_ip();
    await checkLoggedIn();
    getUserDetails();
    getPostList();
  });

  async function getUserDetails() {
    const fetchedDataRes: any = await fetch(ip + "api/v1/user/" + data.userId, {
      method: "GET",
      headers: { Authorization: "Bearer " + $store_token },
    });
    if (!fetchedDataRes.ok) {
      await goto("/");
    }

    const fetchedData = await fetchedDataRes.json();
    user_name = fetchedData.user_name;
    bio = fetchedData.bio;
    if (bio != "null" && bio != null) {
      bio_update = bio; // For the modal pre-input
    } else {
      bio_update = "";
    }
  }

  async function getPostList() {
    const fetchedDataRes = await fetchPage("api/v1/post/user/"+ data.userId +"/" , "GET", page);

    for(const post of fetchedDataRes) {
      post.avatarSrc = avatarSrc;
      post.date = await formatDate(post.date);
    }

    postList = postList.concat(fetchedDataRes);

  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 500);
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
        Authorization: "Bearer " + $store_token,
      },
    });
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

    /*
    This code is used to retrieve the user's avatar from the server. The avatar serves as a profile picture stored on the server.
    To begin, the code sends a request to the server in order to fetch the avatar. The server responds by providing an image file.
    Next, the code loads the image file as a blob and utilizes the FileReader API to convert it into a data URL.
    Finally, the data URL is utilized to set the src attribute of the img element, displaying the user's avatar.*/

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
      };
    }
    loadAvatar();
   
    
  });

  onMount(async () => {
    window.onscroll = function (ev) {
      if (canScroll) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight - 500
      ) {
        page += 1;
        scrollTimeout();
        getPostList();
      }
    }};
  });

  async function update_bio() {
    const res = await fetcher("api/v1/user/update/" + bio_update, "PUT");
  }
</script>

<ScrollButton></ScrollButton>

<div class="container mx-auto pt-5 pb-3 w-11/12 md:max-w-3xl lg:max-w-5xl sm:w-full">
  <div class="flex items-center pl-2 py-2.5 bg-ui border border-border rounded-lg w-full">
    {#if avatarSrc !== undefined}
      <img src={avatarSrc} alt="Avatar" width="75" height="75" class="rounded-full">
    {:else}
      <img src={defaultAvatar} alt="Avatar" width="75" height="75" class="rounded-full">
    {/if}

    <div class="pl-3">
      <span class="font-semibold">{user_name}</span>

      {#if bio !== null && bio !== "null"}
        <p class="pt-1 mr-1 break-words">{bio}</p>
      {/if}
    </div>

    {#if $store_userid === data.userId}
      <div class="flex-1 flex justify-end">
        <button class="hover:bg-hover rounded" on:click={toggle}>
          <svg fill="#ffffff" height="50px" width="50px" version="1.1" xmlns="http://www.w3.org/2000/svg" viewBox="-306.64 -306.64 919.92 919.92" xml:space="preserve">
            <g id="SVGRepo_bgCarrier" stroke-width="0"/>
            <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>    
            <g id="SVGRepo_iconCarrier"> <g> <g> <path d="M12.809,238.52L0,306.637l68.118-12.809l184.277-184.277l-55.309-55.309L12.809,238.52z M60.79,279.943l-41.992,7.896 l7.896-41.992L197.086,75.455l34.096,34.096L60.79,279.943z"/> <path d="M251.329,0l-41.507,41.507l55.308,55.308l41.507-41.507L251.329,0z M231.035,41.507l20.294-20.294l34.095,34.095 L265.13,75.602L231.035,41.507z"/> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> <g> </g> </g> </g> 
          </svg>
        </button>
      </div>
    {/if}
  </div>

  <div class={open ? "block pt-5" : "hidden"}>
    <div class="bg-postBG border border-border p-4 rounded-lg max-w-5xl">
      <div class="text-lg font-semibold mb-2">Beschreibung bearbeiten:</div>
        <div class="mb-6">
          <textarea class="text-white bg-ui border border-border rounded-lg w-full resize-none" maxlength="255" bind:value={bio_update}/>
        </div>

      <hr class="h-0.5 border-t-0 bg-text" />

      <div class="text-lg font-semibold pt-3 mb-2">Profilbild bearbeiten:</div>
      <div class="mb-4">
        <input type="file" name="file" id="AvatarFile" bind:this={avatar_file} on:change={handleFileChange}/>
      </div>

      <div class="flex justify-end space-x-2">
        <button class="dangerButton" on:click={toggle}>Abbrechen</button>
        <button class="primaryButton" on:click={toggle} on:click={update_bio} on:click={upload_avatar}>Profil aktualisieren</button>
      </div>
    </div>
  </div>
</div>

{#if postList[0] != undefined}
  {#each postList as post (post.id)}
    <PostItem post={post} avatarSrc={avatarSrc}/>
  {/each}
{/if}


