# Events
Some events are spicy!

## Executar o Projeto
* Para executar o projeto, será necessário instalar o [Android Studio 4.1.1 ou superior](https://developer.android.com/studio?authuser=1&hl=en-us)
* Uma coisa muito importante sobre esse repositório é que ele possui submodules! Então você precisa clonar os submodules para que tudo funcione!
* Use o comando: `git clone --recurse-submodules -j8 git://github.com/ForceTower/testing-events-app.git` para clonar o repositorio e todos os seus submodules de uma só vez.
* Se você fez um git clone sem submodules, execute o comando `git submodule update --init` dentro da pasta do projeto clonado :)

## Submodules? Por que?
O submodule `android-toolkit` é uma coleção de códigos uteis que eu acumulei ao longo do tempo, então, para evitar reescrita ou vários copia e cola de codigo entre projetos, eles tem seu proprio repositório que recebe atualizações constantes
Contudo, como estou adicionando bastante código "desnecessário" em alguns projetos, o uso do Proguard para minimificar, obfuscar e excluir codigo não utilizado se torna quase obrigatório.

## Arquitetura
Este aplicativo utiliza MVVM como arquitetura principal. 
Cada View Model tem acesso ao um Repository que possui regras de negócio sobre a camada de dados.
O aplicativo possui um banco de dados para armazenamento local dos dados obtidos na nuvem, isso permite que o aplicativo funcione mesmo sem acesso à internet (offline first)

O aplicativo utiliza o [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) (irmão caçula e recem nascido do Dagger) para injeção de dependencias, escolhi o Hilt pois gosto do dagger, da verificação em tempo de compilação do grafo das dependencias (que evita surpresas em runtime), e por ele seguir a especificação JSR330 para injeção de dependências.
Um dos pontos negativos do Dagger é que o setup dele é... complicado, verboso e muitas das vezes repetitivo entre projetos.
O Hilt resolve este problema permitindo ter todas as funções do Dagger (inclusive você pode usar o modelo dagger + hilt em projetos) sem precisar escrever N linhas de codigo para criar um component :)

A cobertura de testes unitários dos repositorios e das view models usando a biblioteca `Mockk`

Como o Android 4.4 (API 19) precisava ser suportado, a biblioteca de conexão com o backend, OkHttp, é diferente entre os APK's da API 19 e 21.
O OkHttp antigo não tem suporte à novas tecnologias de conexão, então para uma melhor experiencia em dispositivos Android 5.0+ (API 21) instale o APK correto :)

## Features
- O aplicativo apresenta uma lista de eventos
- Clicar em um evento você é direcionado aos detalhes dele, 
- O usuário pode fazer check-in no evento!
- O usuário compartilhar o evento com seus amigos!

## Bibliotecas usadas
* [AndroidX](http://d.android.com/jetpack)
* [Material Components](https://github.com/material-components/material-components-android)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Glide](https://github.com/bumptech/glide)
* [Retrofit](https://github.com/square/retrofit)
* [Mockk](https://github.com/mockk/mockk)
