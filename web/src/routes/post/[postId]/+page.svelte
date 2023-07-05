<script lang="ts">
  import { onMount } from "svelte";
  import { fetchPage, fetcher, formatDate, getCookie } from "../../../lib/functions";
  import { error } from "@sveltejs/kit";
  import { goto } from "$app/navigation";
  import type { Snapshot } from "@sveltejs/kit";
  import { default as defaultAvatar } from "../../../lib/assets/defaultAvatar.png";
  import { fetchProfilePicture } from "../../../lib/functions";
  import { store_token, store_userid, store_username } from "$lib/stores";
  let ip: string;
  let canScroll = true;

  export const snapshot: Snapshot = {
    capture: () => comment_text,
    restore: (value) => (comment_text = value),
  };

  export let data: any;



  let userID: string;
  let comment_text: string;
  let comment_list: any = [];
  let page: number = 0;

  let title: string;
  let content: string;
  let date: string;
  let user_name: string;
  let thisID: any = data.postId;
  let title_update: string;
  let content_update: string;
  let isEdited: boolean;
  let avatarSrc: any;
  let buttonPressed = false;

  let imageSrc: any = null;
  let file: any;
  let image_file: File;

  let content_update_c : string;

  let open = false;
  const toggle = () => (open = !open);

  let open_c: { [key: string]: boolean } = {}; 
  let toggle_c: { [key: string]: () => void } = {}; 

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }
  async function checkLoggedIn() {
    $store_username = await getCookie("username");
    $store_token = await getCookie("tokenValue");
    $store_userid = await getCookie("userid");
  }

 

  onMount(async () => {
    await get_server_ip();
    await checkLoggedIn();
    await getPost();
    await getComments();
  });

  async function getPost() {
    await checkLoggedIn();
    const fetchedDataRes = await fetch(ip + "api/v1/post/" + thisID, {
      method: "GET",
      headers: { Authorization: "Bearer " + $store_token },
    });

    if (!fetchedDataRes.ok) {
      await goto("/");
    }

    const fetchedData = await fetchedDataRes.json();
    title = fetchedData.title;
    content = fetchedData.content;
    date = await formatDate(fetchedData.date);
    user_name = fetchedData.user_name;
    userID = fetchedData.user_id;
    title_update = title;
    content_update = content;
    isEdited = fetchedData.edited;
    avatarSrc = await fetchProfilePicture(ip, $store_token, fetchedData);
  }

  function createToggleFunction(commentId : string) {
    return function() {
      for(const comment of comment_list) {
        if(comment.id != commentId) {
          open_c[comment.id] = false;
        }
      }

      open_c[commentId] = !open_c[commentId];
      content_update_c = comment_list.find((comment : any) => comment.id == commentId).content;
    };
  }
 
  async function getComments() {
    const fetchedRes = await fetchPage("api/v1/comment/" + thisID + "/","GET", page)

    for (const comment of fetchedRes) {
      comment.avatarSrc = await fetchProfilePicture(ip, $store_token, comment);
      comment.date = await formatDate(comment.date);
      open_c[comment.id] = false;
      toggle_c[comment.id] = createToggleFunction(comment.id);

    }

    comment_list = comment_list.concat(fetchedRes);
  }



  async function update_post() {
   
    const res = await fetcher("api/v1/post/" + thisID,"PUT",{
      id: thisID,
      title: title_update,
      content: content_update,
      user_id: $store_userid,
      user_name: $store_username,
      date: "2023-03-04 14:00:05.0",
    })

    await update_image();
    await goto("/");
    await goto("/post/" + thisID);
    return res.json();
  }
  

  async function post_comment() {
    if(comment_text == null) {
      return;
    }
    buttonPressed = true;
    const res = await fetcher("api/v1/comment","POST",{ 
      content: comment_text,
        post_id: thisID,
      })
    location.reload();
    if(!res.ok) {
      buttonPressed = false
    }
    return res;
  }

  async function del_comment(commentId : string) {
    const res = await fetcher("api/v1/comment/" + commentId ,"DELETE",{})
    location.reload();
    return res;
  }

  async function update_comment(commentId : string){
    const res = await fetcher("api/v1/comment/" + commentId,"PUT",{
      id: commentId,
      content: content_update_c,
      user_id: $store_userid,
      user_name: $store_username,
      date: "2023-03-04 14:00:05.0",
    })

    location.reload();
    return res;
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 500);
  }

  onMount(async () => {
    window.onscroll = function (ev) {
      // Dynamic site loading
      if (canScroll)
        if (
          window.innerHeight + window.pageYOffset >=
          document.body.offsetHeight - 500
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
        Authorization: "Bearer " + $store_token,
      },
    });
  }

  async function del_post() {
    const res = await fetcher("api/v1/post/" + thisID,"DELETE")
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

        {#if isEdited}
          <span class="pl-1 pt-3.5 text-text text-sm">• (Bearbeitet)</span>
        {/if}
      </div>
      
      <p class="break-words whitespace-pre-line leading-relaxed font-semibold text-xl py-2"> {title}</p>

      <p class="break-words whitespace-pre-line leading-relaxed">{content}</p>
    
      {#if imageSrc != undefined}
        <img class="mt-5 mb-5" src={imageSrc} alt="image"/>
      {/if}
      
      {#if $store_userid === userID}
        <div class="py-5">
          <button class="text-white bg-primary hover:brightness-75 px-4 py-2 rounded" on:click={toggle}>Post bearbeiten</button>
          <button class="text-white bg-red-500 hover:brightness-75 px-4 py-2 rounded ml-2" on:click={del_post}>Post löschen</button>
        </div>
      {/if}
    </div>
  </div>

  <div class={open ? "block pt-5" : "hidden"}>
    <div class="bg-postBG border border-border p-4 rounded-lg max-w-5xl">
      <div class="text-lg font-semibold mb-2">Titel:</div>
      <div class="mb-6">
        <textarea class="text-white bg-ui border border-border rounded-lg w-full resize-none" maxlength="255" bind:value={title_update}/>
      </div>

      <div class="text-lg font-semibold mb-2">Inhalt:</div>
      <div class="mb-6">
        <textarea class="text-white bg-ui border border-border rounded-lg w-full" bind:value={content_update}/>
      </div>
    
      <hr class="h-0.5 border-t-0 bg-text" />
    
      <div class="text-lg font-semibold pt-3 mb-2">Bild:</div>
        <div class="mb-4">
          <input type="file" name="file" id="AvatarFile" bind:this={image_file} on:change={handleFileChange}/>
        </div>
    
        <div class="flex justify-end">
          <button class="bg-red-500 hover:brightness-75 text-white px-4 py-2 rounded" on:click={toggle}>Abbrechen</button>
          <button class="bg-primary hover:brightness-75 text-white px-4 py-2 rounded ml-2" on:click={toggle} on:click={update_post}>Post aktualisieren</button>
        </div>
      </div>
    </div>
  </div>

  <div class="bg-gray-900 text-white pt-5">
    <form>
      <div class="container mx-auto max-w-5xl">
        <h3 class="text-lg font-bold py-3">Kommentare:</h3>
        <div class="mt-2">
          <textarea id="comment_text" class="border border-border bg-postBG rounded w-full" placeholder="Kommentar hinzufügen…" bind:value={comment_text} style="height: 100px"></textarea>
          <div class="flex justify-end mt-3">
            {#if !buttonPressed}
              <button class="bg-ui hover:bg-hover py-2 px-4 rounded-full" on:click={post_comment}>Post Comment</button>
            {:else}
              <button class="bg-ui hover:bg-hover py-2 px-4 rounded-full" on:click={post_comment} disabled>Post Comment</button>
            {/if}
          </div>
        </div>
      </div>
    </form>
  </div>

{#each comment_list as comment (comment.id)}
  <div class="container mx-auto py-5 max-w-5xl">
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
          <a class="pl-2 pt-3.5 text-text text-sm" href={"/profile/" + comment.user_id}>{comment.user_name}</a>
          <span class="pl-1 pt-3.5 text-text text-sm">• {comment.date}</span>

          {#if comment.edited}
            <span class="pl-1 pt-3.5 text-text text-sm">• (Bearbeitet)</span>
          {/if}
        </div>
    
        <p class="break-words whitespace-pre-line leading-relaxed">{comment.content}</p>

        {#if $store_userid === userID}
          <div class="py-5">
            <button class="text-white bg-primary hover:brightness-75 px-4 py-2 rounded" on:click={() => toggle_c[comment.id]()}>Kommentar bearbeiten</button>
            <button class="text-white bg-red-500 hover:brightness-75 px-4 py-2 rounded ml-2" on:click={() => del_comment(comment.id)}>Kommentar löschen</button>
          </div>
        {/if}
      </div>
    </div>

    <div class={open_c[comment.id] ? "block pt-5" : "hidden"}>
      <div class="bg-postBG border border-border p-4 rounded-lg max-w-5xl">
  
        <div class="text-lg font-semibold mb-2">Inhalt:</div>
        <div class="mb-6">
          <textarea class="text-white bg-ui border border-border rounded-lg w-full" bind:value={content_update_c}/>
        </div>
      
        <div class="flex justify-end">
          <button class="bg-red-500 hover:brightness-75 text-white px-4 py-2 rounded" on:click={() => toggle_c[comment.id]()}>Abbrechen</button>
            <button class="bg-primary hover:brightness-75 text-white px-4 py-2 rounded ml-2" on:click={() => toggle_c[comment.id]} on:click={() => update_comment(comment.id)}>Kommentar aktualisieren</button>
        </div>
      </div>
    </div>
  </div>
  {/each}


