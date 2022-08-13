import { createSlice } from "@reduxjs/toolkit"

const initialState = {
    value:0
}

export const todoListCounterSlice = createSlice({
    name: 'todoListCounter',
    initialState,
    reducers: {
        add: state => {
            state.value += 1
        }
    }
})

export const {add} = todoListCounterSlice.actions

export default todoListCounterSlice.reducer