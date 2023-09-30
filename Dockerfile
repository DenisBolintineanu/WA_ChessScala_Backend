# Use OpenJDK 11 as the base image
FROM openjdk:11

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk && \
    apt-get install -y libsass-dev

# Install Scala 2.13.1
RUN wget https://downloads.lightbend.com/scala/2.13.1/scala-2.13.1.tgz && \
    tar -xvzf scala-2.13.1.tgz && \
    mv scala-2.13.1 /usr/local/scala-2.13.1 && \
    echo "export PATH=$PATH:/usr/local/scala-2.13.1/bin" >> ~/.bashrc

# Install Scala 3 (e.g., 3.0.0)
RUN wget https://github.com/lampepfl/dotty/releases/download/3.0.0/scala3-3.0.0.tar.gz && \
    tar -xvzf scala3-3.0.0.tar.gz && \
    mv scala3-3.0.0 /usr/local/scala3-3.0.0 && \
    echo "export PATH=$PATH:/usr/local/scala3-3.0.0/bin" >> ~/.bashrc

# Install SBT
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee -a /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee -a /etc/apt/sources.list.d/sbt_old.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install sbt


# Set the working directory
WORKDIR /ChessScala
ADD . /ChessScala

ENV SBT_OPTS="-Xmx2G -Xms256M -Xss2M"

# Compile and run the application
CMD sbt compile && sbt start
