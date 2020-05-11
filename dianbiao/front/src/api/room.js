import axios from 'axios';

import { prefix } from './apiConfig';


export const search = (param) => { return axios.post(`${prefix}/room/search`,param); };

export const del = (param) => { return axios.post(`${prefix}/room/del`,param); };

export const add = (param) => { return axios.post(`${prefix}/room/add`,param); };