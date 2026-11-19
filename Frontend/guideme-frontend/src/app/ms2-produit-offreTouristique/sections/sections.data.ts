export interface Block {
  id: string;
  title: string;
  text: string;
  points: string[];
  img: string;
}

export interface Section {
  key: string;
  label: string;
  kicker: string;
  title: string;
  lead: string;
  hero: string;
  blocks: Block[];
  cta: { title: string; text: string; button: string; link: string };
}

const IMG = {
  carthage: '/img/carthage.jpg',
  sidi: '/img/sidi.jpg',
  sahara: '/img/sahara.jpg',
  nature: '/img/aindraham.jpg',
};

/** Contenu éditorial des entrées du menu public : il présente Guidini et son offre. */
export const SECTIONS: Section[] = [
  {
    key: 'the-camp',
    label: 'The Camp',
    kicker: 'Qui sommes-nous',
    title: 'Guidini, le camp de base de votre voyage en Tunisie',
    lead: 'Guidini rassemble au même endroit les voyageurs et les professionnels du tourisme tunisien, pour que chaque séjour soit simple à organiser et authentique à vivre.',
    hero: IMG.sidi,
    blocks: [
      {
        id: 'notre-histoire',
        title: 'Notre histoire',
        text: 'Guidini est né d’un constat : la Tunisie regorge d’expériences uniques, mais elles sont dispersées et difficiles à réserver. Nous avons voulu une plateforme unique, claire et fiable.',
        points: ['Une seule plateforme pour activités, circuits et offres', 'Des prix affichés clairement, sans surprise', 'Une mise en avant des acteurs locaux'],
        img: IMG.carthage,
      },
      {
        id: 'equipe',
        title: 'L’équipe',
        text: 'Une équipe passionnée de développeurs, de designers et de connaisseurs du terrain qui travaille chaque jour à améliorer l’expérience des voyageurs comme des prestataires.',
        points: ['Conception centrée sur l’utilisateur', 'Support disponible 24/7', 'Amélioration continue grâce à vos retours'],
        img: IMG.nature,
      },
      {
        id: 'professionnels-verifies',
        title: 'Professionnels vérifiés',
        text: 'Chaque prestataire est contrôlé avant d’être publié : identité, activité et qualité de service. Les avis des voyageurs complètent cette vérification.',
        points: ['Validation des produits et offres par notre équipe', 'Avis authentiques de voyageurs ayant réservé', 'Traitement rapide des réclamations'],
        img: IMG.sahara,
      },
    ],
    cta: { title: 'Envie de nous rejoindre ?', text: 'Voyageur ou professionnel du tourisme, créez votre compte en quelques minutes.', button: 'Créer un compte', link: '/register' },
  },
  {
    key: 'trips',
    label: 'Trips',
    kicker: 'Voyager autrement',
    title: 'Des circuits pensés pour découvrir la Tunisie',
    lead: 'Du nord au sud, choisissez un itinéraire prêt à partir ou composez le vôtre avec des guides et des chauffeurs de confiance.',
    hero: IMG.sahara,
    blocks: [
      {
        id: 'circuits',
        title: 'Circuits',
        text: 'Des itinéraires de plusieurs jours qui relient sites antiques, médinas et paysages du désert, avec transport et guide inclus selon l’offre.',
        points: ['Du Cap Bon au Grand Sud', 'Guide francophone, anglophone ou arabophone', 'Petits groupes pour plus de convivialité'],
        img: IMG.sahara,
      },
      {
        id: 'week-ends',
        title: 'Week-ends',
        text: 'Deux ou trois jours pour s’évader : Tunis et Carthage, Sidi Bou Saïd, Hammamet ou Aïn Draham, selon vos envies.',
        points: ['Départs flexibles', 'Idéal pour les résidents comme pour les visiteurs', 'Réservation immédiate'],
        img: IMG.sidi,
      },
      {
        id: 'sur-mesure',
        title: 'Sur mesure',
        text: 'Dites-nous ce que vous aimez : les prestataires vous proposent un programme adapté à votre rythme, votre budget et votre groupe.',
        points: ['Itinéraire construit avec le prestataire', 'Missions de guide ou de chauffeur à la carte', 'Devis clair avant toute réservation'],
        img: IMG.carthage,
      },
    ],
    cta: { title: 'Prêt à partir ?', text: 'Parcourez les circuits disponibles et réservez en quelques clics.', button: 'Voir les offres', link: '/touriste' },
  },
  {
    key: 'accommodation',
    label: 'Accommodation',
    kicker: 'Se poser',
    title: 'Des hébergements pour chaque façon de voyager',
    lead: 'Hôtel face à la mer, maison d’hôtes de charme ou nuit sous les étoiles : trouvez l’endroit qui prolonge votre expérience.',
    hero: IMG.sidi,
    blocks: [
      {
        id: 'hotels',
        title: 'Hôtels',
        text: 'Une sélection d’établissements pour tous les budgets, des adresses de centre-ville aux stations balnéaires.',
        points: ['Emplacements proches des sites à visiter', 'Conditions d’annulation affichées', 'Offres promotionnelles régulières'],
        img: IMG.sidi,
      },
      {
        id: 'maisons-d-hotes',
        title: 'Maisons d’hôtes',
        text: 'Des demeures traditionnelles tenues par des hôtes passionnés, pour goûter à l’art de recevoir tunisien.',
        points: ['Cadre authentique et chaleureux', 'Petit-déjeuner et cuisine locale', 'Conseils personnalisés de votre hôte'],
        img: IMG.carthage,
      },
      {
        id: 'camps-du-desert',
        title: 'Camps du désert',
        text: 'Passez une nuit dans les dunes, sous un ciel étoilé, après une journée à dos de dromadaire ou en 4×4.',
        points: ['Dîner et nuit sous tente inclus', 'Lever de soleil sur les dunes', 'Encadrement par des guides locaux'],
        img: IMG.sahara,
      },
    ],
    cta: { title: 'Un séjour sur mesure ?', text: 'Associez hébergement, activités et circuits dans une même réservation.', button: 'Explorer', link: '/touriste' },
  },
  {
    key: 'experiences',
    label: 'Experiences',
    kicker: 'Vivre la Tunisie',
    title: 'Des expériences authentiques, signées par des locaux',
    lead: 'Culture, saveurs et bord de mer : des moments à partager avec ceux qui connaissent le mieux leur région.',
    hero: IMG.carthage,
    blocks: [
      {
        id: 'culture',
        title: 'Culture',
        text: 'Visites de Carthage, de Dougga ou de la médina de Tunis : plongez dans plus de trois mille ans d’histoire avec un guide passionné.',
        points: ['Sites antiques et musées', 'Artisanat et médinas', 'Visites à thème et ateliers'],
        img: IMG.carthage,
      },
      {
        id: 'gastronomie',
        title: 'Gastronomie',
        text: 'Marchés, cours de cuisine et tables d’hôtes : apprenez les recettes de couscous, brik ou harissa chez ceux qui les préparent depuis toujours.',
        points: ['Cours de cuisine traditionnelle', 'Dégustations et marchés locaux', 'Repas chez l’habitant'],
        img: IMG.sidi,
      },
      {
        id: 'mer-et-plage',
        title: 'Mer & Plage',
        text: 'Plages de sable fin, criques et îles : détente, sorties en bateau et découvertes du littoral méditerranéen.',
        points: ['Sorties en mer et snorkeling', 'Plages et stations balnéaires', 'Couchers de soleil à ne pas manquer'],
        img: IMG.sidi,
      },
    ],
    cta: { title: 'Choisissez votre expérience', text: 'Des dizaines d’activités vous attendent, notées par les voyageurs.', button: 'Découvrir', link: '/touriste' },
  },
  {
    key: 'activities',
    label: 'Activities',
    kicker: 'Bouger',
    title: 'Des activités pour tous les rythmes',
    lead: 'Balades en forêt, randonnées, quad dans le désert ou sports nautiques : l’aventure est à portée de réservation.',
    hero: IMG.nature,
    blocks: [
      {
        id: 'nature',
        title: 'Nature',
        text: 'Forêts de chênes-lièges, lacs et montagnes du nord-ouest : des randonnées accessibles et des paysages inattendus.',
        points: ['Randonnées à Aïn Draham', 'Observation de la faune et de la flore', 'Sorties encadrées pour tous niveaux'],
        img: IMG.nature,
      },
      {
        id: 'aventure',
        title: 'Aventure',
        text: 'Dromadaire, 4×4 ou quad dans le Sahara : des sensations fortes avec des guides qui connaissent chaque piste.',
        points: ['Expéditions d’un à plusieurs jours', 'Équipement et sécurité assurés', 'Groupes de taille réduite'],
        img: IMG.sahara,
      },
      {
        id: 'sports-nautiques',
        title: 'Sports nautiques',
        text: 'Plongée, jet-ski, kitesurf ou paddle : la Méditerranée est un terrain de jeu idéal presque toute l’année.',
        points: ['Initiations et sorties confirmées', 'Matériel fourni par le prestataire', 'Moniteurs qualifiés'],
        img: IMG.sidi,
      },
    ],
    cta: { title: 'Envie de bouger ?', text: 'Filtrez par catégorie et réservez l’activité qui vous ressemble.', button: 'Voir les activités', link: '/touriste' },
  },
];
