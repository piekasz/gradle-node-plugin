package com.github.gradle.node.npm.proxy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class NpmProxyTest {
    @AfterEach
    internal fun tearDown() {
        GradleProxyHelper.resetProxy()
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenNoProxyIsConfigured() {
        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).isEmpty()
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenAnHttpProxyIsConfiguredWithoutUserPassword() {
        GradleProxyHelper.setHttpProxyHost("1.2.3.4")
        GradleProxyHelper.setHttpProxyPort(8123)

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--proxy", "http://1.2.3.4:8123")
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenAnHttpProxyIsConfiguredWithUserPassword() {
        GradleProxyHelper.setHttpProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpProxyPort(1234)
        GradleProxyHelper.setHttpProxyUser("me/you")
        GradleProxyHelper.setHttpProxyPassword("p@ssword")

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--proxy", "http://me%2Fyou:p%40ssword@4.3.2.1:1234")
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenAnHttpsProxyIsConfiguredWithoutUserPassword() {
        GradleProxyHelper.setHttpsProxyHost("1.2.3.4")
        GradleProxyHelper.setHttpsProxyPort(8123)

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--https-proxy", "https://1.2.3.4:8123")
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenAnHttpsProxyIsConfiguredWithUserPassword() {
        GradleProxyHelper.setHttpsProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpsProxyPort(1234)
        GradleProxyHelper.setHttpsProxyUser("me/you")
        GradleProxyHelper.setHttpsProxyPassword("p@ssword")

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--https-proxy", "https://me%2Fyou:p%40ssword@4.3.2.1:1234")
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenBothAnHttpAndHttpsProxyAreConfigured() {
        GradleProxyHelper.setHttpProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpProxyPort(1234)
        GradleProxyHelper.setHttpsProxyHost("1.2.3.4")
        GradleProxyHelper.setHttpsProxyPort(4321)
        GradleProxyHelper.setHttpsProxyUser("me/you")
        GradleProxyHelper.setHttpsProxyPassword("p@ssword")

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--proxy", "http://4.3.2.1:1234",
                "--https-proxy", "https://me%2Fyou:p%40ssword@1.2.3.4:4321")
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenThePortIsNotDefined() {
        GradleProxyHelper.setHttpProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpsProxyHost("4.3.2.1")

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).isEmpty()
    }

    @Test
    internal fun shouldComputeTheProxyArgsWhenThePassword() {
        GradleProxyHelper.setHttpProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpProxyPort(80)
        GradleProxyHelper.setHttpProxyUser("me")
        GradleProxyHelper.setHttpsProxyHost("4.3.2.1")
        GradleProxyHelper.setHttpsProxyPort(443)
        GradleProxyHelper.setHttpsProxyHost("me")

        val result = NpmProxy.computeNpmProxyCliArgs()

        assertThat(result).containsExactly("--proxy", "http://4.3.2.1:80", "--https-proxy", "https://me:443")
    }
}
