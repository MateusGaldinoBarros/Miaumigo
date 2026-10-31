import { Heart } from "lucide-react";
import { stories, team } from "../Home/data";
import { SectionHeading } from "../Home/shared";

function HeroHistorias() {
  return (
    <section className="historias-hero">
      <div className="historias-hero__text">
        <h1 className="historias-hero__title">
          Histórias que mudaram{" "}
          <span className="historias-hero__title--accent">vidas</span>{" "}
        </h1>
        <p className="historias-hero__desc">
          Veja abaixo relatos reais de quem encontrou um novo melhor amigo
          através do MIAUmigos.
        </p>
        <div className="historias-hero__actions">
          <button className="btn btn--primary">Quero fazer meu match</button>
        </div>
      </div>

      <div className="historias-hero__images">
        <img
          className="historias-hero__main-img"
          src="/pessoafeliz.png"
          alt="Pessoa abraçando cachorro"
        />
      </div>
    </section>
  );
}
function QuemEstaPorTras() {
  return (
    <section className="quem-section">
      <div className="quem-section__header">
        <h2 className="quem-section__title">Quem está por trás do MIAUmigos</h2>
        <span className="quem-section__deco" aria-hidden="true">— /</span>
      </div>

      <div className="quem-grid">
        {team.map((member) => (
          <div className="quem-card" key={member.name}>
            <img
              className="quem-card__avatar"
              src={member.photo}
              alt={member.name}
            />
            <strong className="quem-card__name">{member.name}</strong>
            {member.role && (
              <span className="quem-card__role">{member.role}</span>
            )}
            {member.bio && (
              <p className="quem-card__bio">{member.bio}</p>
            )}
          </div>
        ))}
      </div>
    </section>
  );
}

// relatos
function Relatos() {
  return (
    <section className="relatos-section">
      <div className="relatos-section__header">
        <div>
          <h2 className="relatos-section__title">
            Relatos de quem encontrou um novo melhor amigo
          </h2>
          <p className="relatos-section__sub">
            Histórias reais de amor, companheirismo e novos começos.
          </p>
        </div>
        <button className="btn btn--outline-round">Ver mais histórias &rsaquo;</button>
      </div>

      <div className="story-grid story-grid-home">
        {stories.map((story) => (
          <article className="story-card story-card-photo" key={story.title}>
            <img
              className="story-main-photo"
              src={story.tutorImage}
              alt={`Tutor em ${story.title}`}
            />
            <div>
              <small>História de amor</small>
              <h3>{story.title}</h3>
              <p>{story.text}</p>
              <strong>{story.author}</strong>
            </div>
          </article>
        ))}
      </div>

      <div className="story-dots" aria-hidden="true">
        {[...Array(5)].map((_, i) => (
          <span key={i} className={`story-dot${i === 0 ? " story-dot--active" : ""}`} />
        ))}
      </div>
    </section>
  );
}

function VoceTambemPodeMudar() {
  return (
    <section className="cta-mudar">
      <img
        className="cta-mudar__pets"
        src="/gatoecachorro-imagem.png"
        alt="Cachorro e gato"
      />
      <div className="cta-mudar__content">
        <h2 className="cta-mudar__title">
          Você também pode{" "}
          <span className="historias-hero__title--accent">mudar uma vida</span>{" "}
        </h2>
        <p className="cta-mudar__desc">
          Milhares de pets estão esperando por alguém como você. Que tal ser o
          próximo capítulo de uma história incrível?
        </p>
        <button className="btn btn--primary btn--lg">
          Fazer meu match agora
        </button>
      </div>
    </section>
  );
}

export default function Historias({ homeBlock = false }) {
  if (homeBlock) {
    return (
      <section className="content-section soft-band">
        <SectionHeading
          eyebrow="Adoções que transformaram vidas"
          title="Relatos de quem encontrou um novo amigo"
          text="Histórias reais com tutores e pets que ganharam uma nova rotina juntos."
        />
        <Relatos />
      </section>
    );
  }

  return (
    <div className="historias-page">
      <HeroHistorias />
      <QuemEstaPorTras />
      <Relatos />
      <VoceTambemPodeMudar />
    </div>
  );
}
