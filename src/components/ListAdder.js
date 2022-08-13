import React from "react";
import "../styles/ListAdder.css";
import "../styles/Button.css";
import { useSelector, useDispatch } from "react-redux";
import { add } from "../redux/slices/todoListCounterSlice";

const ListAdder = ({ buttonName, buttonClass }) => {
  const count = useSelector((state) => state.todoListCounter.value);
  const dispatch = useDispatch();

  return (
    <div className="container" id="listAdder">
      <textarea
        name="newItem"
        id="newItemName"
        cols="30"
        rows="10"
        placeholder="type here"
      ></textarea>
      <div className="container">
        <button
          className={buttonClass}
          type="submit"
          onClick={() => {
            if (document.getElementById("newItemName").value.length > 0) {
              dispatch(add());
              document.getElementById(
                "todoList"
              ).innerHTML += `<li><span class='itemName'>${
                document.getElementById("newItemName").value
              }</span><span class='close'>x</span></li>`;
              document.getElementById("newItemName").value = ``;
            }
          }}
        >
          {buttonName}
        </button>
      </div>
      <h2>TODO List count: {count}</h2>
    </div>
  );
};

export default ListAdder;
