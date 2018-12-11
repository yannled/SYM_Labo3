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

   







