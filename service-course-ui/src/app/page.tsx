import type { BikesQuery } from '__generated__/graphql';
import { gql, useQuery } from '@apollo/client';

const BIKES_QUERY = gql`
  query Bikes {
    bikes {
      edges {
        node {
          id
        }
      }
    }
  }
`;

const Home = () => {
  const { data } = useQuery<BikesQuery>(BIKES_QUERY);

  console.warn(data);

  return <h1>Hello There!</h1>;
};

export default Home;
