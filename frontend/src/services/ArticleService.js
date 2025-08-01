import axios from 'axios';

const REST_API_BASE_URL = `${import.meta.env.VITE_API_URL}/articles`;

/**
 * Retrieves articles fetched within the last 24-hours.
 * Articles will be ordered by the time at which they were published or edited. If the article's
 * publish/edit time is not available, it will be placed at the end of the list.
 * @returns {Promise<axios.AxiosResponse<
 * {
 *     articleUrl: String,
 *     category: String,
 *     id: BigInt,
 *     imageUrl: String,
 *     source: String,
 *     timeFetched: String,
 *     timePublished: String,
 *     title: String,
 * }[], status: Number>>}
 */
export const getRecentArticles = () => axios.get(
    `${REST_API_BASE_URL}/recent/lite`);

/**
 * Retrieves an article by its ID.
 * @param {BigInt} id - The ID of the article to retrieve.
 * @returns {Promise<axios.AxiosResponse<{
 *     articleUrl: String,
 *     body: String,
 *     category: String,
 *     id: BigInt,
 *     imageUrl: String,
 *     source: String,
 *     timeFetched: String,
 *     timePublished: String,
 *     title: String,
 * }, status: Number>>}
 */
export const getArticleById = (id) => axios.get(
    `${REST_API_BASE_URL}/${id}`);
