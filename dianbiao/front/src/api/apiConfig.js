/**
 * @apiDefine responseSuccessCommon 接口通用返回字段
 * @apiSuccess {Number} status 状态, 0:成功,非0:失败
 * @apiSuccess {String} msg  消息
 * @apiSuccess {Object} data  数据
 */

/**
 * @apiDefine responseErrorCommon 接口通用返回字段
 * @apiError {Number} status 状态, 1:失败
 * @apiError {String} msg  消息
 * @apiError {Object} data  数据
 */

/**
 * @apiDefine paginationParams 分页接口请求字段
 * @apiParam {Number} [offset] 查询结果偏移, 默认值0
 * @apiParam {Number} [limit] 查询结果限制条数, 默认值10, -1:表示无限制
 */

export const prefix = process.env.NODE_ENV === 'development'? 'api':'';