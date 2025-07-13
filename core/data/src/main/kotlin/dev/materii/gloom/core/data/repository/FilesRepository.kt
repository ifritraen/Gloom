package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.fragment.File
import dev.materii.gloom.core.graphql.fragment.RawMarkdownFile
import dev.materii.gloom.core.graphql.fragment.TreeFragment
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform

interface FilesRepository {

    /**
     * Get a list of files and folders in a repo at
     * a given path in a given branch
     *
     * @param owner The repository owner
     * @param name The repository name
     * @param branch Branch to look for the path in
     * @param path Desired path to browse through
     */
    suspend fun getTree(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<TreeFragment?>

    /**
     * Get a file and its contents
     *
     * @param owner The repository owner
     * @param name The repository name
     * @param branch Branch to look for the path in
     * @param path Path to the desired file
     */
    suspend fun getFile(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<File?>

    /**
     * Gets the raw, unrendered markdown
     *
     * @param owner The repository owner
     * @param name The repository name
     * @param branch Branch to look for the path in
     * @param path Path to the markdown file
     */
    suspend fun getRawMarkdown(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<RawMarkdownFile?>

}

internal class FilesRepositoryImpl(
    private val graphQL: GraphQLDataSource
): FilesRepository {

    override suspend fun getTree(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<TreeFragment?> {
        return graphQL.getTree(owner, name, "$branch:$path").transform {
            it.repository?.gitObject?.treeFragment
        }
    }

    override suspend fun getFile(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<File?> {
        return graphQL.getFile(owner, name, branch, path).transform {
            it.repository?.file
        }
    }

    override suspend fun getRawMarkdown(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<RawMarkdownFile?> {
        return graphQL.getRawMarkdown(owner, name, branch, path).transform {
            it.repository?.gitObject?.onCommit?.file?.fileType?.rawMarkdownFile
        }
    }
}