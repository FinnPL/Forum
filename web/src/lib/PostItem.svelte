<script lang="ts">
    import { goto } from "$app/navigation";
    import { default as defaultAvatar } from "./assets/defaultAvatar.png";
    import { formatContentWithLinks } from "./functions";

    /**
    * @type {{ id: string; user_id: string; avatarSrc: any; title: string; user_name: string; date: any; content: string; }}
    */
    export let post:any ;
    export let avatarSrc:any;

</script>
  

<div class="container mx-auto max-h-96 pt-5 w-11/12 md:max-w-3xl lg:max-w-5xl sm:w-full">
    <a class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border hover:border-hover" href={"/post/" + post.id} >
      <div>
        <div class="font-semibold flex">
          <a href={"/profile/" + post.user_id}>
            {#if avatarSrc}
              <img class="rounded-full" src={avatarSrc} alt="Avatar" width="50" height="50" />
            {:else}
              {#if post.avatarSrc}
                <img class="rounded-full" src={post.avatarSrc} alt="Avatar" width="50" height="50" />
              {:else}
              <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
              {/if} 
            {/if}
          </a>
          <a class="pl-2 pt-3.5 text-text text-sm" href={"/profile/" + post.user_id} >{post.user_name}  </a>
          <span class="pl-1 pt-3.5 text-text text-sm">• {post.date}</span>
          {#if post.edited}
            <span class="pl-1 pt-3.5 text-text text-sm">• (Bearbeitet)</span>
          {/if}
        </div>
        
        <p class="break-words whitespace-pre-line font-semibold text-xl py-2"> {post.title}</p>
  
        <p class="break-words whitespace-pre-line leading-relaxed line-clamp-5">{@html formatContentWithLinks(post.content)}</p>
        <br />
      </div>
    </a>
</div>

