<script lang="ts">
  import { onMount } from "svelte";
  import { fetchPage, fetcher, formatContentWithLinks, formatDate, getCookie } from "../../../lib/functions";
  import { error } from "@sveltejs/kit";
  import { goto } from "$app/navigation";
  import type { Snapshot } from "@sveltejs/kit";
  import { default as defaultAvatar } from "../../../lib/assets/defaultAvatar.png";
  import { fetchProfilePicture } from "../../../lib/functions";
  import { store_token, store_user_role, store_userid, store_username } from "$lib/stores";
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
    getPost();
    getComments();
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
    content = formatContentWithLinks( fetchedData.content);
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
    location.reload();
    return res.json();
  }
  

/*This is the function that is called when the comment button is pressed.
 It will send a POST request to the server with the content of the comment and the post id of the post it is associated with.
 The server will then create the comment and save it to the database. After the request is complete, the page will refresh.*/

  async function post_comment() {
    if(comment_text == null) {
      return;
    }
    buttonPressed = true;
    const res = await fetcher("api/v1/comment","POST",{ 
      content: comment_text,
        post_id: thisID,
      })
    
    if(!res.ok) {
      buttonPressed = false
    }
    comment_text = "";
    location.reload();
    
    return res;
  }

   /*This code deletes a comment from the database. It takes a commentId as a parameter and sends a DELETE request to the API with the commentId. 
   It then reloads the page.*/
  async function del_comment(commentId : string) {
    const res = await fetcher($store_user_role === 'ADMIN' ? `api/v1/admin/comment/${commentId}` : `api/v1/comment/${commentId}`, "DELETE");
    location.reload();
    return res;
  }

  //This code updates a comment by making a PUT request to the API endpoint. It then reloads the page.
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



// This code runs when the user scrolls to the bottom of the page.
// It checks to see if the user has scrolled to the bottom of the page
// and if they have, it loads more comments.

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

/*This code deletes a post from the database. This is done by sending a DELETE request to the API endpoint for deleting a post, 
and then redirecting the user to the home page.*/

  async function del_post() {
    const res = await fetcher($store_user_role === 'ADMIN' ? `api/v1/admin/post/${thisID}` : `api/v1/post/${thisID}`, "DELETE");
    await goto("/");
  }
</script>

{#if content !== undefined}
  <div class="container mx-auto pt-5 w-11/12 md:max-w-3xl lg:max-w-5xl sm:w-full">
    <div class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border">
      <div>
        <div class="font-semibold flex items-center text-sm text-text space-x-1">
          <a href={"/profile/" + userID}>
            <img class="rounded-full" src={avatarSrc ? avatarSrc : defaultAvatar} alt="Avatar" width="50" height="50" />
          </a>
          <a class="pl-1" href={"/profile/" + userID}>{user_name}</a>
          <span>• {date}</span>
        
          <span hidden={!isEdited}>• (Bearbeitet)</span>
        </div>
      
        <div class="break-words whitespace-pre-line leading-relaxed">
          <p class="text-xl py-2 font-semibold">{title}</p>
          <p class="pb-4">{@html content}</p>
        </div>

        <img class="mb-4" hidden={!imageSrc} src={imageSrc} alt="image"/>
        
        {#if $store_userid === userID || $store_user_role === "ADMIN"}
          <div class="pb-4 space-x-2 text-sm">
            <button class="primaryButton" hidden={$store_userid !== userID} on:click={toggle}>Bearbeiten</button>
            <button class="dangerButton" on:click={del_post}>Löschen</button>
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
      
          <div class="flex justify-end space-x-2">
            <button class="dangerButton" on:click={toggle}>Abbrechen</button>
            <button class="primaryButton" on:click={toggle} on:click={update_post}>Post aktualisieren</button>
          </div>
        </div>
      </div>
    </div>

    <form>
      <div class="container mx-auto w-11/12 md:max-w-3xl lg:max-w-5xl sm:w-full bg-gray-900 text-white py-5">
        <h3 class="text-lg font-bold py-3">Kommentare:</h3>
        <div class="mt-2">
          <textarea id="comment_text" class="border border-border bg-postBG rounded w-full" placeholder="Kommentar hinzufügen…" bind:value={comment_text} style="height: 100px"></textarea>
          <div class="flex justify-end mt-3">
            <button class="bg-ui hover:bg-hover py-2 px-4 rounded-full" on:click={post_comment} disabled={buttonPressed}>Kommentar posten</button>
          </div>
        </div>
      </div>
    </form>
{/if}

{#each comment_list as comment (comment.id)}
  <div class="container mx-auto py-5 w-11/12 md:max-w-3xl lg:max-w-5xl sm:w-full">
    <div class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border hover:border-hover">
      <div>
        <div class="font-semibold text-xl flex">
          <a href={"/profile/" + comment.user_id}>
            <img class="rounded-full" src={comment.avatarSrc ? comment.avatarSrc : defaultAvatar} alt="Avatar" width="50" height="50" />
          </a>
          <a class="pl-2 pt-3.5 text-text text-sm" href={"/profile/" + comment.user_id}>{comment.user_name}</a>
          <span class="pl-1 pt-3.5 text-text text-sm">• {comment.date}</span>
          <span class="pl-1 pt-3.5 text-text text-sm" hidden={!comment.edited}>• (Bearbeitet)</span>
        </div>
    
        <p class="break-words whitespace-pre-line leading-relaxed py-2">{@html formatContentWithLinks(comment.content)}</p>

        {#if $store_userid === comment.user_id || $store_user_role === "ADMIN"}
          <div class="pt-2 pb-4 space-x-2 text-sm">
            <button class="primaryButton" hidden={$store_userid !== comment.user_id} on:click={() => toggle_c[comment.id]()}>Bearbeiten</button>
            <button class="dangerButton" on:click={() => del_comment(comment.id)}>Löschen</button>
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
      
        <div class="flex justify-end space-x-2 text-sm">
          <button class="dangerButton" on:click={() => toggle_c[comment.id]()}>Abbrechen</button>
          <button class="primaryButton" on:click={() => toggle_c[comment.id]} on:click={() => update_comment(comment.id)}>Kommentar aktualisieren</button>
        </div>
      </div>
    </div>
  </div>
{/each}


