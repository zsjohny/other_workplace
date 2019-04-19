export default function (actionName) {
  // return (process.env.NODE_ENV !== 'production' ? '' : 'https://local.nessary.top:8081/publicaccount') + actionName
  return (process.env.NODE_ENV !== 'production' ? '' : 'https://local.yujiejie.com/publicaccount') + actionName
}

