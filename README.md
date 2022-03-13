# Eventos

<img src="imgs/one.gif"  width="270" height="570"> <img src="imgs/two.gif"  width="270" height="570">

## Onde estão os APKs??
Você pode encontrar os APK's na aba `Actions` aqui do github, escolha a ultima versão concluída com exito e baixe os artefatos lá :)

## Executar o Projeto
* Para executar o projeto, será necessário instalar o [Android Studio Bumblebee | 2021.1.1 Patch 2 ou superior](https://developer.android.com/studio?authuser=1&hl=en-us)

## Arquitetura
Este aplicativo utiliza MVVM como arquitetura principal.
Cada View Model tem acesso aos UseCases necessários para as funcionalidades propostas. um Repository que possui regras de negócio sobre a camada de dados.
O aplicativo possui um banco de dados para armazenamento local dos dados obtidos na nuvem, isso permite que o aplicativo funcione mesmo sem acesso à internet (offline first)

O aplicativo utiliza o [Hilt](https://d.android.com/hilt) (irmão do Dagger) para injeção de dependências, escolhi o Hilt pois gosto do dagger, da verificação em tempo de compilação do grafo das dependências (que evita surpresas em runtime), e por ele seguir a especificação JSR330 para injeção de dependências.
Um dos pontos negativos do Dagger é que o setup dele é... complicado, verboso e muitas das vezes repetitivo entre projetos.
O Hilt resolve este problema permitindo ter todas as funções do Dagger (inclusive você pode usar o modelo dagger + hilt em projetos) sem precisar escrever N linhas de codigo para criar um component :)

A cobertura de testes unitários dos usecases e das view models usando a biblioteca `Mockk`

Como o Android 4.4 (API 19) precisava ser suportado, a biblioteca de conexão com a internet, OkHttp, é diferente entre os APK's da API 19 e 21+.
O OkHttp antigo não tem suporte à novas tecnologias de conexão, então para uma melhor experiencia em dispositivos Android 5.0+ (API 21) instale o APK correto :)

## Features
- O aplicativo apresenta uma lista de eventos.
- Clicar em um evento você é direcionado aos detalhes dele.
- O usuário pode fazer check-in no evento!
- O usuário compartilhar o evento com seus amigos!
- O usuário pode ver o local do evento no mapa :)

## Principais bibliotecas usadas
* [AndroidX](http://d.android.com/jetpack)
* [Material Components](https://github.com/material-components/material-components-android)
* [Hilt](https://d.android.com/hilt)
* [Glide](https://github.com/bumptech/glide)
* [Retrofit](https://github.com/square/retrofit)
* [OkHttp](https://square.github.io/okhttp)
* [Mockk](https://github.com/mockk/mockk)
