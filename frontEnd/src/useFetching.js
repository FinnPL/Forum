import { useState, useEffect } from 'react';

const useFetching = (url) => {
        const [data, setData] = useState(null);
        useEffect(() => {
            fetch(url)
                .then(res => {return res.json();})
                 .then(data => {setData(data);})},[]);
        return({data});
    }

export default useFetching;