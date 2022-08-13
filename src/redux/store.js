import { configureStore } from "@reduxjs/toolkit";
import todoListCounterReducer from "./slices/todoListCounterSlice";

export const store = configureStore({
  reducer: {
    todoListCounter: todoListCounterReducer,
  },
});
