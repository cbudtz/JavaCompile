import React from 'react';
import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"
const apiurl = process.env.NODE_ENV ? "http://localhost:8080/api/openapi" : "/api/openapi"

function App() {
  return (
    <div className="App">
        <SwaggerUI url={apiurl}/>
    </div>
  );
}

export default App;
