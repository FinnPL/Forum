import { Link } from "react-router-dom";
import * as React from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const PostList = ({ posts }) => {
  return (
    <div className="post-list">
      {posts.map((post) => (
        <div className="post-preview" key={post.id}>
          <Link to={`/post/${post.id}`}>
            <h2>{post.title}</h2>
            <p>Gepostet von {post.author}</p>
            <br></br>
            <p>{post.body}</p>
          </Link>
        </div>
      ))}
    </div>
  );
};

export default PostList;
