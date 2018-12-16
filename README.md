# SYM_Labo3
### NFC

*les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8
dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément
copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC.*

*A partir de l’API Android concernant les tags NFC 4 , pouvez-vous imaginer une autre approche pour
rendre plus compliqué le clonage des tags NFC ? Est-ce possible sur toutes les plateformes (Android et
iOS), existe-il des limitations ? Voyez-vous d’autres possibilités ?*

Une des possiblité pour compliquer le clonage nfc est d'utiliser l'ID. Une partie des tags nfc possède un id unique (UID) ou un Random ID (RID) . On peut alors imaginer la situation où l'on utilise une validation d'authentification par NFC et au lieu de simplement vérifier que les données sur la puce nfc sont les bonnes, on vérifie aussi que l'ID est le bon, valide. Cette démarche permet de s'assurer que le tag nfc n'est pas un copie mais bel et bien le Tag prévu pour l'authentification de l'utilisateur donné. Malheureusement il peut être possible de modifier l'ID d'un tag, donc ce n'est pas totalement fiable. Cette démarche est disponible seulement à partir de l'API 10 d'Android.

Une autre sécurité contre le clonage de tag est d'utiliser un algorithme de chiffrement entre le tag et l'application qui le lit. L'idée est que l'application possède une clé permettant de déchiffrer le contenu du TAG et ce dernier permet d'authentifier l'utilisateur.

Pour ce qui est de l'utilisation du NFC avec android et IOS :

- Sur android ces possiblité sont disponibles depuis l'API 10.

- Sur IOS, c'est à partir de iOS 11 que l'on peut utiliser le NFC, par contre il peut lire uniquement des message correctement codés sous forme de NDEF. Il n'est pas capable de lire l'ID unique du tag ni d'encoder des données sur un tag. Cela bloque alors l'utilisation d'un ID ou d'échange cryptographique pour éviter le clonage.

   

### Codes-barres

*Comparer la technologie à codes-barres et la technologie NFC, du point de vue d'une utilisation dans
des applications pour smartphones, dans une optique :*

- *Professionnelle (Authentification, droits d’accès, stockage d’une clé)*
- *Grand public (Billetterie, contrôle d’accès, e-paiement)*
- *Ludique (Preuves d'achat, publicité, etc.)*
- *Financier (Coûts pour le déploiement de la technologie, possibilités de recyclage, etc.)*

Le code barre permet de stocker de l'information plus largement que la technologie NFC. En effet il ne permet pas simplement de contenir un identifiant comme le fait NFC, mais permet d'y encoder plus d'information. Le code barre sous forme de QRCode peut contenir beaucoup d'information comme du texte ou des urls.

Le code-barre est très pratique pour accélérer le temps de lecture d'une information en effet sont modèle d'encodage pouvant être lu facilement et sans erreur par une machine évite de faire l'entrée manuellement par une personne. Ce système est donc adapté dans des cas quand l'information doit être lue rapidement ou qu'elle est fastidieuse à introduire. Comme par exemple les numéros d'articles ou des liens spécifiques vers des pages web.

L'information encodée ne doit cependant pas être confidentielle ou doit pouvoir être gardée secrète. Les codes barres peuvent facilement être copié, dupliqué ou fabriqué. Ce procédé n'est donc pas adapté pour faire de l'authentification. Par exemple pour un badge d'entreprise qui permet de déverrouiller un ordinateur avec un second facteur ou d'ouvrir une porte la technologie NFC offre une sécurité accrue au code barre avec une protection contre la copie.

Dans une domaine public ou la ressource est moins valorisée on voit l'utilisation du code barre plus répandue. En effet pour identifier un compte bancaire, au lieu d'entrer le numéro à la main on peut utiliser un code barre pour accélérer la saisie, mais ne devrait pas être utiliser pour s'identifier auprès de la banque. Pour tout ce qui est des billets d'entrée ce procédé permet de valider qu'un billet n'est pas utilisé plusieurs fois. Il est alors à la charge de l'acheteur de s'assurer que sont billet n'est pas copié. Dans tous les cas le billet sera utilisé qu'une fois. Cela reste cependant moins critique qu'une carte d'accès qui est utilisé souvent et pour faire l'accès à une ressource critique, comme un bâtiment.

Pour tout ce qui est de la distribution d'information comme la publicité, le code barre est très adapté car cette information est publique et ce nécessite donc aucune protection spécifique.

Le coût que représente la production d'un code barre est pratiquement nulle et accessible à tout le monde. Il ne nécessite rien de plus qu'un moyen d'afficher l'information. Un écran ou une feuille de papier sont suffisant. Il est relativement compliqué de recyclé un code barre vu que celui-ci contient une information exacte. Il est plus judicieux de régénérer un nouveau code avec l'information nécessaire. Le NFC quand à lui pourra être recyclé facilement en associant l'id à une nouvelle ressource.

### iBeacon

*Les iBeacons sont très souvent présentés comme une alternative à NFC. Pouvez-vous commenter cette*
*affirmation en vous basant sur 2-3 exemples de cas d’utilisations (use-cases) concrets (par exemple e-*
*paiement, second facteur d’identification, accéder aux horaires à un arrêt de bus, etc.).*

Les deux technologies sont similaire sur leur manière de s'authentifier. En effet les deux utilisent un ID unique permettant de les identifier. Cependant leurs cas d'utilisation est sensiblement différent. La technologie NFC se base sur la proximité de lecture alors que le iBeacon permet l'authentification dans une zone plus large. 

Le NFC offre l'avantage de la détection de proximité. Cela lui permet d'identifier de manière unique un utilisateur seulement s'il présente sa carte. Si celle-ci est tenue à l'écart du lecteur il est impossible d'en effectuer la lecture. Cette propriété offre une sécurité relativement convenable. Il est également possible de protéger les puces contre la copie ce qui en font un très bon token d'identification.
Cette technologie est donc appropriée pour des cas d'utilisation qui nécessitent de la sécurité comme les moyen de payement ou d'authentification de personne. Par exemple dans les cartes bancaire ou les badges d'entreprise permettant d'ouvrir des portes.

Le iBeacon est quant à lui détectable à une certaine distance et nécessite une alimentation. Cette technologie est plus propice pour l'identification d'un lieu ou d'une zone spécifique. Il n'est pas nécessaire d'être en contact direct avec le terminal. Il n'est donc pas possible de savoir de manière sûre quel iBeacon est sélectionné. Il est cependant possible de savoir environs à quel distance la borne se trouve du terminal et donc d'estimer lequel choisir. 
Cette technologie est appropriée pour localiser la position du terminal. En fonction de la pièce dans laquelle se trouve le terminal on peut définir un comportement spécifique. Par exemple de définir des endroit de confiance et ne pas demander le mot de passe tant que le iBeacon est détecté. Ou bien de sélectionner directement un arrêt de bus si le terminal se trouve dans la zone de l'arrêt en question.

L'utilisation des deux technologies s'applique donc à des situations différentes en fonction des besoins. Je ne pense donc pas qu'il soit possible de dire que le iBeacon est une solution qui pourrait remplacer le NFC mais qui apporte des possibilités complémentaires.

### lCapteur

*vous constaterez que les animations de la flèche ne sont pas* *fluides, il va y avoir un tremblement plus ou moins important même si le téléphone ne bouge pas.*
*Veuillez expliquer quelle est la cause la plus probable de ce tremblement et donner une manière *

Deux causes possibles, dépendantes de l'interprétation du sens du tremblement: 

* tremblement dû aux informations du capteur pas assez précises, le capteur transmettant des changements de 
  position minimes qui n'existent soit pas (phantom movements), soit parce que le téléphone bouge effectivement mais d'une façon imperceptible. 
  **Solution**: 

  application d'un filtre passe-bas sur les données du capteur, ayant pour agréable conséquence d'ignorer toutes valeurs exotiques. exemple : 

  ```java
  protected float[] lowPass( float[] input, float[] output ) {
      if ( output == null ) return input;     
      for ( int i=0; i<input.length; i++ ) {
          output[i] = output[i] + ALPHA * (input[i] - output[i]);
      }
      return output;
  }
  ```

  https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings

* tremblement dû au dessin de la flèche : comme on détruit l'image avant même de calculer la nouvelle,  on peut avoir 
  une impression de tremblement. 
  **Solution**: 

  gérer différement l'affichage entre chaque image : soit techniquement dans la fonction de drawing (check par rapport au framerate, ..), soit définir un delta maximum entre deux matrices de rotation (par exemple l'angle entre deux vecteurs unités transformé par nos matrices respectivement) et calculer une nouvelle matrice de rotation correspondant à ce delta maximum, puis afficher la flèche en fonction.  





