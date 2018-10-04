# Spring upgrade issue

Here a sample project where all is working id using spring 5.0.9 but i have an error with spring 5.1.0
The test class is in src/test/java and it's it.olegna.spring.upgrade.tests.TestSpringUpgrade

In the pom.xml if you use spring 5.1.0 and spring-security 5.1.0 the test class fails because the Cache Configuration class it.olegna.spring.upgrade.configuration.HibDbCacheConfig is not called