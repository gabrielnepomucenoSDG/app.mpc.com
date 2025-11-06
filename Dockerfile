# ---- Estágio 1: O "Build" (Construção) ----
# Usamos uma imagem que já tem o Maven e o Java (JDK)
FROM maven:3.8.5-openjdk-22 AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Roda o comando de build do Maven (igual ao que faríamos antes)
RUN mvn package -DskipTests

# ---- Estágio 2: O "Run" (Execução) ----
# Usamos uma imagem leve, que só tem o Java (JRE) para rodar
FROM openjdk:22-jre-slim

WORKDIR /app

# Copia APENAS o JAR executável que foi gerado no Estágio 1
# IMPORTANTE: Ajuste o nome do .jar abaixo!
COPY --from=build /app/target/narraterpro-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot usa (padrão 8080)
EXPOSE 8080

# O comando para iniciar seu app
ENTRYPOINT ["java", "-jar", "app.jar"]