import { useParams } from "react-router-dom";
import useFetching from "./useFetching";
import * as React from "react";

const Onepost = () => {
  const { id } = useParams();
  const { data: postData } = useFetching("http://localhost:8000/posts/" + id);
  return (
    <div className="one-post">
      {postData && (
        <article>
          <h2>
            {postData.title} - {id}
          </h2>
          <h3>Geschrieben von {postData.author}</h3>
          <p>{postData.body}</p>
        </article>
      )}
    </div>
  );
};
export default Onepost;
