import { ApolloClient, ApolloProvider, InMemoryCache } from '@apollo/client';
import type { Metadata } from 'next';
import { Inter } from 'next/font/google';

const inter = Inter({ subsets: ['latin'] });

const client = new ApolloClient({
  uri: 'http://localhost:8080',
  cache: new InMemoryCache({
    typePolicies: {
      Product: {
        // In an inventory management system, products might be identified
        // by their UPC.
        keyFields: ['upc'],
      },
    },
  }),
});

export const metadata: Metadata = {
  title: 'Service Course',
};

const RootLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <html lang="en">
      <body className={inter.className}>
        <ApolloProvider client={client}>{children}</ApolloProvider>,
      </body>
    </html>
  );
};

export default RootLayout;
