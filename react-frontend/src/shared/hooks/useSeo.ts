import { useEffect } from 'react';

/**
 * Placeholder SEO hook.
 * Replaces Angular SeoService (updateMetaFromKeys, setStructuredData, etc.).
 *
 * For full implementation, install react-helmet-async:
 *   npm install react-helmet-async
 *
 * Then wrap <App /> in <HelmetProvider> and use <Helmet> here.
 */
export const useSeo = (config: {
  title?: string;
  description?: string;
  image?: string;
  url?: string;
  type?: string;
}) => {
  useEffect(() => {
    if (config.title) {
      document.title = config.title;
    }

    // Update meta description
    let metaDesc = document.querySelector('meta[name="description"]');
    if (!metaDesc) {
      metaDesc = document.createElement('meta');
      metaDesc.setAttribute('name', 'description');
      document.head.appendChild(metaDesc);
    }
    if (config.description) {
      metaDesc.setAttribute('content', config.description);
    }

    // Open Graph tags
    const setOg = (property: string, content: string) => {
      let tag = document.querySelector(`meta[property="og:${property}"]`);
      if (!tag) {
        tag = document.createElement('meta');
        tag.setAttribute('property', `og:${property}`);
        document.head.appendChild(tag);
      }
      tag.setAttribute('content', content);
    };

    if (config.title) setOg('title', config.title);
    if (config.description) setOg('description', config.description);
    if (config.image) setOg('image', config.image);
    if (config.url) setOg('url', config.url);
    if (config.type) setOg('type', config.type);
  }, [config.title, config.description, config.image, config.url, config.type]);
};