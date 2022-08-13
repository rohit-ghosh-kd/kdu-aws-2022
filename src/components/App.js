import React from "react";
import Header from "./Header";
import "../styles/reset.css";
import "../styles/App.css";
import ListAdder from "./ListAdder";
import ListItems from "./ListItems";

const App = () => {
  return (
    <div>
      <Header name={"TODO List"} className={"mainHeader"}></Header>
      <ListAdder buttonName={"ADD"} buttonClass={"addButton"}></ListAdder>
      <ListItems></ListItems>
    </div>
  );
};

export default App;
