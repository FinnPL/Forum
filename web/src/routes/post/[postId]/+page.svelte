<script lang="ts">
  import { onMount } from "svelte";
  import { token, cookie_name, own_user_id } from "../../../lib/Login/login";
  import { formatDate, getCookie } from "../../../lib/functions";
  import { error } from "@sveltejs/kit";
  import { goto } from "$app/navigation";
  import type { Snapshot } from "@sveltejs/kit";
  import { default as defaultAvatar } from "../../../lib/assets/defaultAvatar.png";
  import { fetchProfilePicture } from "../../../lib/functions";
  let ip: string;
  let canScroll = true;

  export const snapshot: Snapshot = {
    capture: () => comment_text,
    restore: (value) => (comment_text = value),
  };

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
  let isEdited: boolean;
  let avatarSrc: any;

  let imageSrc: any = null;
  let file: any;
  let image_file: File;


  //Modal
  let open = false;
  const toggle = () => (open = !open);

  console.log(thisID);

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }
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
    await get_server_ip();
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
      await goto("/");
      
    }

    const fetchedData = await fetchedDataRes.json();
    console.log(fetchedData);
    title = fetchedData.title;
    content = fetchedData.content;
    date = await formatDate(fetchedData.date);
    user_name = fetchedData.user_name;
    userID = fetchedData.user_id;
    title_update = title;
    content_update = content;
    isEdited = fetchedData.edited;
    avatarSrc = await fetchProfilePicture(ip, tokenValue, fetchedData);
  }

  async function getFirstComments() {
    const fetchedRes = await fetch(
      ip + "api/v1/comment/" + thisID + "/" + page,
      {
        method: "GET",
        headers: { Authorization: "Bearer " + tokenValue },
      }
    );
    const fetchedData = await fetchedRes.json();

    for (const comment of fetchedData) {
      comment.avatarSrc = await fetchProfilePicture(ip, tokenValue, comment);
    }

    comment_list = fetchedData;
  }

  async function getComments() {
    const fetchedRes = await fetch(
      ip + "api/v1/comment/" + thisID + "/" + page,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + tokenValue,
        },
      }
    );
    const fetchedData = await fetchedRes.json();

    for (const comment of fetchedData) {
      comment.avatarSrc = await fetchProfilePicture(ip, tokenValue, comment);
    }

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
    await goto("/");
    await goto("/post/" + thisID);
    return res.json();
  }

  async function post_comment() {
    if(comment_text == null) {
      return;
    }
    const res = await fetch(ip + "api/v1/comment", {
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
    await goto("/");
    await goto("/post/" + thisID);
    return res.json();
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 1000);
  }

  onMount(async () => {
    window.onscroll = function (ev) {
      // Dynamic site loading
      if (canScroll)
        if (
          window.innerHeight + window.pageYOffset >=
          document.body.offsetHeight
        ) {
          page += 1;
          getComments();
          scrollTimeout();
        }
    };
  });

  onMount(async () => {
    let bearerToken = await getCookie("tokenValue");
    let myid = await getCookie("userid");

    console.log(bearerToken);
    let requestOptions: any = {
      method: "GET",
      headers: { Authorization: "Bearer " + bearerToken },
      redirect: "follow",
    };

    const path = window.location.pathname.split("/");
    const post_id = path[path.length - 1]; // Get the url params

    async function loadImage() {
      const res = await fetch(
        ip + "api/v1/file/post/" + post_id + "?" + new Date().getTime(),
        requestOptions
      ); // Add unique params to the image to avoid cache issues
      const blob = await res.blob();
      const reader = new FileReader();
      reader.readAsDataURL(blob);

      if(res.status == 400) {        
        return;
      }

      reader.onloadend = (event) => {
        if (event.target && event.target.result) {
          imageSrc = event.target.result;
        }
      };
    }
    loadImage();

    console.log(imageSrc);
  });

  const handleFileChange = (event: any) => {
    file = event.target.files[0];
  };

  async function update_image() {
    const formData = new FormData();
    formData.append("file", file);
    const res = await fetch(ip + "api/v1/file/post/" + thisID, {
      method: "POST",
      body: formData,
      headers: {
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res);
  }

  async function del_post() {
    const res = await fetch(ip + "api/v1/post/" + thisID, {
      method: "DELETE",
      headers: {
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res);
    await goto("/");
  }
</script>

<div class="container mx-auto pt-5 max-w-5xl">
  <div class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border">
    <div>
      <div class="font-semibold flex">
        <a href={"/profile/" + userID}>
          {#if avatarSrc}
            <img class="rounded-full" src={avatarSrc} alt="Avatar" width="50" height="50" />
          {:else}
            <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
          {/if} 
        </a>
        <a class="pl-2 pt-3.5 text-text text-sm" href={"/profile/" + userID}>{user_name}</a>
        <span class="pl-1 pt-3.5 text-text text-sm">• {date}</span>
      </div>
      
      <p class="break-words whitespace-pre-line leading-relaxed font-semibold text-xl py-2"> {title}</p>

      <p class="break-words whitespace-pre-line leading-relaxed line-clamp-5">{content}</p>
      <br />

      {#if imageSrc != undefined}
        <img class="mt-5" src={imageSrc} alt="image"/>
      {/if}
      

      {#if own_user_id_value == userID}
        <div class="py-5">
          <button class="text-white bg-ui hover:bg-hover px-4 py-2 rounded" on:click={toggle}>Edit Post</button>
          <button class="text-white bg-ui hover:bg-hover px-4 py-2 rounded" on:click={del_post}>Delete Post</button>
          <div class={open ? "block" : "hidden"}>
            <div class="fixed inset-0 flex items-center justify-center">
              <div class="bg-border p-4 rounded w-96">
                <h2 class="text-lg font-bold mb-4">Post bearbeiten</h2>
                <div class="mb-4">
                  <input class="text-black" placeholder="Titel" required bind:value={title_update} />
                </div>
                <div class="mb-4">
                  <textarea class="text-black" placeholder="Body" bind:value={content_update} style="height: 100px"></textarea>
                </div>
                <div class="mb-4">
                  <input id="AvatarFile" type="file" name="file" bind:this={image_file} on:change={handleFileChange} accept="image/png, image/jpeg, image/jpg" />
                </div>
    
                <div class="flex justify-end">
                  <button class="bg-ui hover:bg-hover text-white px-4 py-2 rounded" on:click={toggle}>Cancel</button>
                  <button class="bg-ui hover:bg-hover text-white px-4 py-2 rounded ml-2" on:click={toggle} on:click={update_post} >Post bearbeiten</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      {/if}
    </div>
  </div>
</div>

<div class="bg-gray-900 text-white p-4">
  <form>
    <div class="container mx-auto py-5 max-w-5xl">
      <h3 class="text-lg font-bold">Kommentare:</h3>
      <div class="mt-2">
        <textarea id="comment_text" class="border border-border bg-postBG p-2 rounded w-full" placeholder="Body" bind:value={comment_text} style="height: 100px"></textarea>
        <div class="flex justify-end mt-1">
          <button class="bg-ui hover:bg-hover py-2 px-4 rounded-full" on:click={post_comment}>Post Comment</button>
        </div>
      </div>
    </div>
  </form>
</div>

{#each comment_list as comment (comment.id)}
  <div class="container mx-auto max-h-96 py-5 max-w-5xl">
    <div class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border hover:border-hover">
      <div>
        <div class="font-semibold text-xl flex">
          <a href={"/profile/" + comment.user_id}>
            {#if comment.avatarSrc}
              <img class="rounded-full" src={comment.avatarSrc} alt="Avatar" width="50" height="50" />
            {:else}
              <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
            {/if} 
          </a>
          <a class="pl-5 pt-3.5 text-text text-sm" href={"/profile/" + comment.user_id}>{comment.user_name}</a>
          <span class="pl-1 pt-3.5 text-text text-sm">• {comment.date}</span>
        </div>
    
        <p class="break-words whitespace-pre-line pt-3 leading-relaxed line-clamp-5">{comment.content}</p>
        <br />
      </div>
    </div>
  </div>
{/each}
