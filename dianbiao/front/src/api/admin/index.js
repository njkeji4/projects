import axios from 'axios';

import { prefix } from '../apiConfig';

//user
export const login = params => { return axios.post(`${prefix}/user/login`, params); };
export const logout = () => { return axios.post(`${prefix}/user/logout`); };
export const getUserList = () => { return axios.get(`${prefix}/user/list`); };
export const unlock = params => { return axios.post(`${prefix}/user/unlock`, params); };
export const changePassword = (param) => { return axios.post(`${prefix}/user/changepassword`,param); };
export const addUser = (param) => { return axios.post(`${prefix}/user/add`,param); };
export const removeUsers = (param) => { return axios.post(`${prefix}/user/delete`,param); };

//device
export const getDeviceList = () => { return axios.get(`${prefix}/device/get?_t=${Date.now()}`); };

//export const getDeviceCmdList = () => { return axios.get(`${prefix}/devicecmd/get?_t=${Date.now()}`); };

export const getDeviceCmdList = (param) => { return axios.post(`${prefix}/devicecmd/search`,param); };


export const getDeviceSetting = (param) => { return axios.post(`${prefix}/device/setting/get`,param); };

export const addDevice = (param) => { return axios.post(`${prefix}/device/add`,param); };

export const switchOffDevice = (param) => { return axios.post(`${prefix}/device/off`,param); };

export const switchOnDevice = (param) => { return axios.post(`${prefix}/device/on`,param); };

export const addDeviceSetting = (param) => { return axios.post(`${prefix}/device/setting/add`,param); };

export const searchDevice = (param) => { return axios.post(`${prefix}/device/searchDevice`,param); };

export const searchBaseStation = (param) => { return axios.post(`${prefix}/basestation/searchDevice`,param); };

export const analyzeBaseStation = (param) => { return axios.post(`${prefix}/basestation/analyze`,param); };

export const getBaseAnalyzeResult = () => { return axios.get(`${prefix}/basestation/analysisresult?_t=${Date.now()}`); };

export const deleteDevices = (param) => { return axios.post(`${prefix}/device/del`,param); };

export const updateDevice = (param) => { return axios.post(`${prefix}/device/update`,param); };

export const searchStat = (param) => { return axios.post(`${prefix}/devicestat/search`,param); };

export const getOverview = (param) => { return axios.post(`${prefix}/devicestat/overview`,param); };

export const getOverviewMonthdays = (param) => { return axios.get(`${prefix}/devicestat/overview/monthdays/${param}`); };

//device group
export const getDeviceGroups = () => { return axios.get(`${prefix}/devicegroup/list?_t=${Date.now()}`); };
export const addGroup = (param) => { return axios.post(`${prefix}/devicegroup/add`,param); };
export const editGroup = (param) => { return axios.post(`${prefix}/devicegroup/update`,param); };
export const removeGroup = (param) => { return axios.post(`${prefix}/devicegroup/delete`,param); };

//check data
export const searchCheckData = (param) => { return axios.post(`${prefix}/checkdata/search`,param); };

export const getSpotImg = (id) => { return axios.get(`${prefix}/checkdata/spotimg?id=${id}&_t=${Date.now()}`); };

//version
export const getVersionList = () => { return axios.get(`${prefix}/version/list?_t=${Date.now()}`); };

export const getExistedVersions = () => { return axios.get(`${prefix}/version/existed?_t=${Date.now()}`); };

export const removeVersion = (param) => { return axios.post(`${prefix}/version/delete`,param); };

export const uploadUrl = `${prefix}/device/upload`;
export const websocketurl = `${prefix}/websocket`;

export const uploadBaseStationUrl = `${prefix}/basestation/upload`;

//setting
export const getSetting = () => { return axios.get(`${prefix}/setting/get?_t=${Date.now()}`); };

export const updateSetting = (param) => { return axios.post(`${prefix}/setting/update`,param); };

//blacklist
export const getBlackList = () => { return axios.get(`${prefix}/blacklist/list?_t=${Date.now()}`); };

export const searchBlackList = (param) => { return axios.post(`${prefix}/blacklist/list`, param); };

export const updateBlacklist = (param) => { return axios.post(`${prefix}/blacklist/add`,param); };

export const removeBlacklist = (param) => { return axios.post(`${prefix}/blacklist/delete`,param); };

export const exportBlackList = () => { return axios.get(`${prefix}/blacklist/export?_t=${Date.now()}`); };

export const uploadBlackListUrl = `${prefix}/blacklist/upload`;

//today
export const getTodayData = () => { return axios.get(`${prefix}/device/todayData?_t=${Date.now()}`); };

//alarm
export const getAlarmList = () => { return axios.get(`${prefix}/alarm/list?_t=${Date.now()}`); };

export const searchAlarmData = (param) => { return axios.post(`${prefix}/alarm/search`,param); };

//conf

export const getBaseConf = () => { return axios.get(`${prefix}/baseconf/get?_t=${Date.now()}`); };

export const setBaseConf = (param) => { return axios.post(`${prefix}/baseconf/set`,param); };