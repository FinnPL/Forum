<script>
    import { default as defaultAvatar } from "./assets/defaultAvatar.png";

    /**
    * @type {{ id: string; user_id: string; avatarSrc: any; title: any; user_name: any; date: any; content: any; }}
    */
    export let post;
</script>
  

<div class="container mx-auto max-h-96 pt-5 max-w-5xl">
    <a class="bg-postBG flex rounded-md px-5 pt-5 border-2 border-border hover:border-hover" href={"/post/" + post.id}>
      <div>
        <div class="font-semibold flex">
          <a href={"/profile/" + post.user_id}>
            {#if post.avatarSrc}
              <img class="rounded-full" src={post.avatarSrc} alt="Avatar" width="50" height="50" />
            {:else}
              <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
            {/if} 
          </a>
          <a class="pl-2 pt-3.5 text-text text-sm" href={"/profile/" + post.user_id}>{post.user_name}</a>
          <span class="pl-1 pt-3.5 text-text text-sm">• {post.date}</span>
        </div>
        
        <p class="break-words whitespace-pre-line leading-relaxed font-semibold text-xl py-2"> {post.title}</p>
  
        <p class="break-words whitespace-pre-line leading-relaxed line-clamp-5">{post.content}</p>
        <br />
      </div>
    </a>
</div>

